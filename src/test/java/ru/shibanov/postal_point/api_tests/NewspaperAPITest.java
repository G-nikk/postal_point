package ru.shibanov.postal_point.api_tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import ru.shibanov.postal_point.repositories.NewspaperRepository;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

@TestComponent
public class NewspaperAPITest {

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    public void E2ETest() {
        String newspaperJson = "{ \"name\": \"Новая газета\", \"indexEdition\": \"NG123\", \"editor\": \"Редактор Редакторов\", \"price\": 35.50 }";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(newspaperJson)
                .when()
                .post("/newspapers")
                .then()
                .statusCode(201);

        int newspaperId = RestAssured
                .get("/newspapers")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .and()
                .body(matchesJsonSchemaInClasspath("schemas/all_newspapers_schema.json"))
                .and()
                .body(containsStringIgnoringCase("Новая газета"))
                .extract()
                .path("find { it.name == 'Новая газета' }.newspaperID");

        RestAssured.given()
                .when()
                .get("/newspapers/" + newspaperId)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/newspaper_schema.json"))
                .body("newspaperID", equalTo(newspaperId))
                .body("indexEdition", equalTo("NG123"))
                .body("editor", equalTo("Редактор Редакторов"))
                .body("price", equalTo(35.5F));

        String updatedJson = "{ \"newspaperID\": " + newspaperId + ", \"name\": \"Обновленная газета\", \"indexEdition\": \"NG123\", \"editor\": \"Редактор Редакторов\", \"price\": 35.50 }";
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(updatedJson)
                .when()
                .put("/newspapers/" + newspaperId)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/newspaper_schema.json"))
                .body("name", equalTo("Обновленная газета"));

        RestAssured.given()
                .when()
                .delete("/newspapers/" + newspaperId)
                .then()
                .statusCode(204);
    }

    @Test
    public void testGetAllNewspapers() {
        RestAssured
                .get("/newspapers")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .and()
                .body(matchesJsonSchemaInClasspath("schemas/all_newspapers_schema.json"));
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4})
    public void testGetNewspaperById(int id) {
        RestAssured.given()
                .when()
                .get("/newspapers/" + id)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/newspaper_schema.json"))
                .body("newspaperID", equalTo(id));
    }

    @Test
    public void testCreateNewspaper() {
        String newspaperJson = "{ \"name\": \"Новая газета\", \"indexEdition\": \"NG123\", \"editor\": \"Редактор Редакторов\", \"price\": 35.50 }";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(newspaperJson)
                .when()
                .post("/newspapers")
                .then()
                .statusCode(201);
    }

    @Test
    public void testUpdateNewspaper() {
        String updatedJson = "{ \"newspaperID\": 1, \"name\": \"Обновленная газета\", \"indexEdition\": \"OG123\", \"editor\": \"Новый Редактор\", \"price\": 40.00 }";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(updatedJson)
                .when()
                .put("/newspapers/1")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/newspaper_schema.json"))
                .body("price", equalTo(40.00f));
    }

    @Test
    public void testDeleteNewspaper() {
        RestAssured.given()
                .when()
                .delete("/newspapers/1")
                .then()
                .statusCode(204);
    }

    @Test
    public void testGetPrintingHouses() {
        RestAssured.given()
                .when()
                .get("/newspapers/1/printing-houses")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/printing_houses_schema.json"));
    }

    @Test
    public void testGetEditorLargestPrintRun() {
        RestAssured.given()
                .queryParam("printingHouseId", 2)
                .queryParam("newspaperId", 2)
                .when()
                .get("/newspapers/editor")
                .then()
                .statusCode(200)
                .contentType(ContentType.TEXT)
                .body(matchesRegex("Петров Петр Петрович"));
    }

    @Test
    public void testGetTotalCost() {
        RestAssured.given()
                .queryParam("newspaperId", 2)
                .when()
                .get("/newspapers/total-cost")
                .then()
                .statusCode(200)
                .body(containsStringIgnoringCase("246000.00"));
    }
}