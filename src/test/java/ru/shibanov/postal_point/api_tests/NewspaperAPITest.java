package ru.shibanov.postal_point.api_tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class NewspaperAPITest {

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
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

    @Test
    public void testGetNewspaperById() {
        RestAssured.given()
                .when()
                .get("/newspapers/1")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/newspaper_schema.json"))
                .body("newspaperID", equalTo(1));
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
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath("schemas/newspaper_schema.json"))
                .body("name", equalTo("Новая газета"));
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
                .queryParam("printingHouseId", 1)
                .queryParam("newspaperId", 1)
                .when()
                .get("/newspapers/editor")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/editor_schema.json"))
                .body("editor", notNullValue());
    }

    @Test
    public void testGetTotalCost() {
        RestAssured.given()
                .queryParam("newspaperId", 1)
                .when()
                .get("/newspapers/total-cost")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/total_cost_schema.json"))
                .body("totalCost", greaterThan(0));
    }
}