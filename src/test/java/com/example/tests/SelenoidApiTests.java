package com.example.tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SelenoidApiTests {

/*
        Arrange / given
        1. make request to https://selenoid.autotests.cloud/status
        Act / When
        2. get response - {"total":20,"used":0,"queued":0,"pending":0,"browsers":
        {"android":{"8.1":{}},
        "chrome":{"100.0":{},"99.0":{}},
        "chrome-mobile":{"86.0":{}},
        "firefox":{"97.0":{},"98.0":{}},
        "opera":{"84.0":{},"85.0":{}}}}
        Assert / Then
        3. check total is 20
*/

    @Test
    void checkTotal() {
        given()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .body("total", is(20));
    }

    @Test
    void checkTotalWithStatusCode() {
        given()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .statusCode(200)
                .body("total", is(20));
    }

    @Test
    void checkStatusWithStatusCode401() {
        get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .statusCode(401);

    }

    @Test
    void check200StatusWithAuth() {
        given()
                .auth().basic("user1", "1234")
                .get("https://selenoid.autotests.cloud/wd/hub/status")
        .then()
                .statusCode(200);

    }

    @Test
    void checkTotalWithoutGiven() {
        get("https://selenoid.autotests.cloud/status")
                .then()
                .body("total", is(20));
    }

    @Test
    void checkTotalWithLogs() {
        given()
                .log().all()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .log().all()
                .body("total", is(20));
    }

    @Test
    void checkTotalWithSomeLogs() {
        given()
                .log().uri()
                .log().body()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .log().status()
                .log().body()
                .body("total", is(20));
    }

    @Test
    void checkChrome() {
        get("https://selenoid.autotests.cloud/status")
                .then()
                .log().body()
                .body("browsers.chrome", hasKey("100.0"));
    }

    @Test
    void checkTotalBadPractice() {
        Response response =
        get("https://selenoid.autotests.cloud/status")
                .then().extract().response();
        System.out.println("Current response is - " + response.asString());

        String needsResponse = "{\"total\":20,\"used\":0,\"queued\":0,\"pending\":0,\"browsers\":\n" +
                "        {\"android\":{\"8.1\":{}},\n" +
                "        \"chrome\":{\"100.0\":{},\"99.0\":{}},\n" +
                "        \"chrome-mobile\":{\"86.0\":{}},\n" +
                "        \"firefox\":{\"97.0\":{},\"98.0\":{}},\n" +
                "        \"opera\":{\"84.0\":{},\"85.0\":{}}}}";


        assertEquals(needsResponse, response.asString());
    }

    @Test
    void checkTotalGoodPractice() {
        int actualTotal =
        get("https://selenoid.autotests.cloud/status")
                .then().extract().path("total");

        System.out.println("Current total is - " + actualTotal);

        int expectedTotal = 20;

        assertEquals(expectedTotal, actualTotal);
    }

}
