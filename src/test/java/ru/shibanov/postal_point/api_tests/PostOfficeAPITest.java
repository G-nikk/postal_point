package ru.shibanov.postal_point.api_tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class PostOfficeAPITest {

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    // JSON-схемы
    private static final String POST_OFFICE_SCHEMA = "schemas/post_office_schema.json";
    private static final String POST_OFFICES_SCHEMA = "schemas/post_offices_schema.json";
    private static final String MOST_RECEIVED_SCHEMA = "schemas/most_received_schema.json";
    private static final String MAX_COST_SCHEMA = "schemas/max_cost_schema.json";

    @Test
    public void testGetAllPostOffices() {
        RestAssured.given()
                .when()
                .get("/post-offices")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(matchesJsonSchemaInClasspath(POST_OFFICES_SCHEMA))
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    public void testGetPostOfficeById() {
        RestAssured.given()
                .when()
                .get("/post-offices/1")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath(POST_OFFICE_SCHEMA))
                .body("postOfficeID", equalTo(1));
    }

    @Test
    public void testCreatePostOffice() {
        String postOfficeJson = """
            {
                "number": "PO-606",
                "address": "Москва, ул. Пушкина, 10"
            }""";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(postOfficeJson)
                .when()
                .post("/post-offices")
                .then()
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath(POST_OFFICE_SCHEMA))
                .body("number", equalTo("PO-606"));
    }

    @Test
    public void testUpdatePostOffice() {
        String updatedJson = """
            {
                "postOfficeID": 1,
                "number": "PO-101-updated",
                "address": "Москва, обновленный адрес"
            }""";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(updatedJson)
                .when()
                .put("/post-offices/1")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath(POST_OFFICE_SCHEMA))
                .body("number", equalTo("PO-101-updated"));
    }

    @Test
    public void testDeletePostOffice() {
        RestAssured.given()
                .when()
                .delete("/post-offices/1")
                .then()
                .statusCode(204);
    }

    @Test
    public void testGetMostReceived() {
        RestAssured.given()
                .when()
                .get("/post-offices/most-received")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath(MOST_RECEIVED_SCHEMA))
                .body("postOfficeID", notNullValue());
    }

    @Test
    public void testGetNewspaperInPrintingHouse() {
        RestAssured.given()
                .queryParam("newspaperId", 1)
                .queryParam("printingHouseId", 1)
                .when()
                .get("/post-offices/newspaper-in-printing-house")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath(POST_OFFICES_SCHEMA));
    }

    @Test
    public void testGetMaxCost() {
        RestAssured.given()
                .when()
                .get("/post-offices/max-cost")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath(MAX_COST_SCHEMA));
    }
}