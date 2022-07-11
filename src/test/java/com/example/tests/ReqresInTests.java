package com.example.tests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class ReqresInTests {
    @Test
    void loginTest() {

        String body = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\" }";


        given()
                .log().uri()
                .log().body()
                .contentType(ContentType.JSON)
                .body(body)
        .when()
                .post("https://reqres.in/api/login")
        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void missingPasswordLoginTest() {

        String body = "{ \"email\": \"eve.holt@reqres.in\"}";


        given()
                .log().uri()
                .log().body()
                .contentType(ContentType.JSON)
                .body(body)
        .when()
                .post("https://reqres.in/api/login")
        .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }
}
