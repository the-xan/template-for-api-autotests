package com.example.projects.demowebshop;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.example.projects.reqres.utils.CustomApiListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@Tag("demowebshop")
public class DemowebshopTests {

    static String
            login = "qaguru@qa.guru",
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
        clearBrowserCookies();
    }

    @Test
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

    @Test
    @DisplayName("")
    void addProductToCartTest() {

        String body = "product_attribute_72_5_18=53" +
                "&product_attribute_72_6_19=54" +
                "&product_attribute_72_3_20=57" +
                "&addtocart_72.EnteredQuantity=1";
        String authCookieValue = "AF92D1DFD49E790EE4CC2D0E85753DE050CB2344B20B0F115A15AE" +
                "A618D6A1E25656143E241BEFA5E2618A0347D5B0D42D4669E9D47859A189847CF3921FDDF61D69B52512514D185B" +
                "7DA5680BB4414B84E6007877C6ED9BC4ADCE09D9D8F2E034E8ED0D422AC960B15260B74C73DCDE10480D96CBC743" +
                "22D18641CBABB3EAFA23E166B9A46E8B33AF57476AC31791CF";

        String cartSize = given()
                .filter(withCustomTemplates())
                .contentType("application/x-www-form-urlencoded")
                .body(body)
                .cookie(authorizationCookieName, authCookieValue)
                .log().all()
                .when()
                .post("/addproducttocart/details/72/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"))
                .body("updateflyoutcartsectionhtml", notNullValue())
                .body("updatetopcartsectionhtml", notNullValue())
                .extract()
                .path("updatetopcartsectionhtml");

        step("Open minimal content, because cookie can be set when site is opened", () ->
                open("/Themes/DefaultClean/Content/images/logo.png"));
        step("Set cookie to to browser", () -> {
            Cookie authCookie = new Cookie(authorizationCookieName, authCookieValue);
            WebDriverRunner.getWebDriver().manage().addCookie(authCookie);
        });

        step("Open main page", () ->
                open(""));
        step("Verify cart size", () ->
                $("#topcartlink .cart-qty").shouldHave(text(cartSize)));
    }

    @Test
    @DisplayName("Add product to card with dynamic cookie auth")
    void addProductToCartWithDynamicCookieTest() {
        String authCookieValue = given()
                .filter(withCustomTemplates())
                .contentType("application/x-www-form-urlencoded")
                .formParam("Email", login)
                .formParam("Password", password)
                .log().all()
                .when()
                .post("/login")
                .then()
                .log().all()
                .statusCode(302)
                .extract().cookie(authorizationCookieName);

        String body = "product_attribute_72_5_18=53" +
                "&product_attribute_72_6_19=54" +
                "&product_attribute_72_3_20=57" +
                "&addtocart_72.EnteredQuantity=1";

        String cartSize = given()
                .filter(withCustomTemplates())
                .contentType("application/x-www-form-urlencoded")
                .body(body)
                .cookie(authorizationCookieName, authCookieValue)
                .log().all()
                .when()
                .post("/addproducttocart/details/72/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"))
                .body("updateflyoutcartsectionhtml", notNullValue())
                .body("updatetopcartsectionhtml", notNullValue())
                .extract()
                .path("updatetopcartsectionhtml");

        step("Open minimal content, because cookie can be set when site is opened", () ->
                open("/Themes/DefaultClean/Content/images/logo.png"));
        step("Set cookie to to browser", () -> {
            Cookie authCookie = new Cookie(authorizationCookieName, authCookieValue);
            WebDriverRunner.getWebDriver().manage().addCookie(authCookie);
        });

        step("Open main page", () ->
                open(""));
        step("Verify cart size", () ->
                $("#topcartlink .cart-qty").shouldHave(text(cartSize)));
    }

}
