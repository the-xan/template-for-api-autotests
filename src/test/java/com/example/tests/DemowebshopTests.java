package com.example.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.restassured.AllureRestAssured;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.example.helpers.CustomApiListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;

public class DemowebshopTests {

    static String login = "qaguru@qa.guru",
            password = "qaguru@qa.guru1",
            authorizationCookieName = "NOPCOMMERCE.AUTH";

    @BeforeAll
    static void configure() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        Configuration.baseUrl = "http://demowebshop.tricentis.com/";
        RestAssured.baseURI = "http://demowebshop.tricentis.com/";
    }

    @AfterEach
    void afterEach() {
        closeWebDriver();
    }

    @Test
    @Tag("demowebshop")
    @DisplayName("Successful authorization demowebshop (UI)")
    void loginTest() {
        step("Open login page", () ->
                open("/login"));

        step("Fill login form", () -> {
            $("#Email").setValue(login);
            $("#Password").setValue(password)
                    .pressEnter();
        });

        step("Verify successful authorization", () ->
                $(".account").shouldHave(text(login)));
    }


    @Test
    @Tag("demowebshop")
    @DisplayName("Successful authorization demowebshop (API + UI)")
    void loginWithApiTest() {

        step("Get cookie by API and set it to browser", () -> {

            String authCookieValue = given()
                    .filter(withCustomTemplates())
                    .contentType("application/x-www-form-urlencoded")
                    //это тело запроса
                    .formParam("Email", login)
                    .formParam("Password", password)

                    // можно тело запроса представить вот в таком виде
                    //.body("Email"+login+"&Password="+password+"&RememberMe=false")
                    // либо через стринг формат
                    //.body(format("Email=%s&Password=%s&RememberMe=false", login, password))

                    .log().all()
                    .when()
                    .post("/login")
                    .then()
                    .log().all()
                    .statusCode(302)
                    .extract().cookie(authorizationCookieName);

            step("Open minimal content, because cookie can be set when site is open", () -> {
                open("/Themes/DefaultClean/Content/styles.css");
            });


            step("Set cookie to the browser", () -> {
                Cookie authCookie = new Cookie(authorizationCookieName, authCookieValue);
                WebDriverRunner.getWebDriver().manage().addCookie(authCookie);
            });
        });


        step("Open main page", () -> {
            open("/");
        });

        step("Verify successful authorization", () -> {
            $(".account").shouldHave(text(login));
        });
    }
}
