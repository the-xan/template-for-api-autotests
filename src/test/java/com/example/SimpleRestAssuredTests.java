package com.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.hamcrest.Matchers.equalTo;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.notNullValue;


public class SimpleRestAssuredTests {

    private static String
            ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
            ".eyJfaWQiOiI2MmJkMmQ0YWQzYjg2YTAwM2Q2N2M4NDAiLCJpYXQiOjE2NTY1NjUwNjcsImV4cCI6MTY1NzE2OTg2N30." +
            "6Xf7hRlVa5WR-OI5h7sgskIjRpW9IzBZiA8sUjg-ZXg",
            INFO_STATUS_CODE_URL = "/api/users/me";


    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-mesto.praktikum-services.ru/";
    }

    @Test
    public void getMyInfoStatusCode() {

        given()
            .auth()
            .oauth2(ACCESS_TOKEN)
            .get(INFO_STATUS_CODE_URL)
        .then()
             .statusCode(200).assertThat().body("data.name", equalTo("Жак-Ив Кусто"));

    }

    @Test
    public void workingWithResponse() {

        File json = new File("src/test/resources/newCard.json");



        Response response =
                given()
                        .header("Content-type", "application/json")
                        .auth().oauth2(ACCESS_TOKEN)
                        .and()
                        .body(json)
                .when()
                        .post("/api/cards");


        response
                .then()
                .assertThat()
                .body("data._id", notNullValue())
                .and()
                .statusCode(201);

        System.out.println(response.asString());
    }





}
