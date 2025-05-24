package ru.shibanov.postal_point.api_tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class PrintingHouseAPITest {

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    // JSON-схемы
    private static final String PRINTING_HOUSE_SCHEMA = "schemas/printing_house_schema.json";
    private static final String PRINTING_HOUSES_SCHEMA = "schemas/printing_houses_schema.json";

    @Test
    public void testGetAllPrintingHouses() {
        RestAssured.given()
                .when()
                .get("/printing-houses")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(matchesJsonSchemaInClasspath(PRINTING_HOUSES_SCHEMA))
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    public void testGetPrintingHouseById() {
        RestAssured.given()
                .when()
                .get("/printing-houses/1")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath(PRINTING_HOUSE_SCHEMA))
                .body("printingHouseID", equalTo(1));
    }

    @Test
    public void testCreatePrintingHouse() {
        String printingHouseJson = """
            {
                "name": "Новая типография",
                "address": "Москва, ул. Печатников, 15"
            }""";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(printingHouseJson)
                .when()
                .post("/printing-houses")
                .then()
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath(PRINTING_HOUSE_SCHEMA))
                .body("name", equalTo("Новая типография"));
    }

    @Test
    public void testUpdatePrintingHouse() {
        String updatedJson = """
            {
                "printingHouseID": 1,
                "name": "Обновленная типография",
                "address": "Москва, обновленный адрес"
            }""";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(updatedJson)
                .when()
                .put("/printing-houses/1")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath(PRINTING_HOUSE_SCHEMA))
                .body("name", equalTo("Обновленная типография"));
    }

    @Test
    public void testDeletePrintingHouse() {
        RestAssured.given()
                .when()
                .delete("/printing-houses/1")
                .then()
                .statusCode(204);
    }
}
