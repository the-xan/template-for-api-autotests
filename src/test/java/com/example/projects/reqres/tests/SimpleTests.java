package com.example.projects.reqres.tests;

import com.example.projects.reqres.model.get.UserFullInfo;
import com.example.projects.reqres.model.get.UserSomeInfo;
import com.example.projects.reqres.model.post.CreateUserResponse;
import com.example.projects.reqres.model.post.UserLogin;
import com.example.projects.reqres.model.post.UserRequest;
import com.example.projects.reqres.steps.UsersSteps;
import com.example.projects.reqres.utils.UserGenerator;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.example.projects.reqres.utils.CustomApiListener.withCustomTemplates;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.BODY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@Tag("Reqres.in")
public class SimpleTests {

    private static final RequestSpecification rq = with()
            .filter(withCustomTemplates())
            .baseUri("https://reqres.in")
            .basePath("/api")
            .log().all()
            .contentType(ContentType.JSON);

    private static final ResponseSpecification rs = new ResponseSpecBuilder()
            .log(BODY)
            .build();

    @Test
    @DisplayName("Simple logging test")
    void loginTest() {

        //add request body without lombok builder
        UserLogin request = new UserLogin();
        request.setEmail("eve.holt@reqres.in");
        request.setPassword("cityslicka");

        given()
                .spec(rq)
                .body(request)
        .when()
                .post("/login")
        .then()
                .spec(rs)
                .body("token", is(notNullValue()));
    }

    @Test
    @DisplayName("Simple negative logging test")
    void missingPasswordLoginTest() {

        //add request body without lombok builder
        UserLogin request = new UserLogin();
        request.setEmail("eve.holt@reqres.in");


        given()
                .spec(rq)
                .body(request)
        .when()
                .post("/login")
        .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    @DisplayName("Test using lambda language to find firstName by email")
    void getUsers() {
        given()
                .spec(rq)
                .when()
                .get("/users?page=2")
                .then()
                .statusCode(200)
                .body("data.find{it.email=='george.edwards@reqres.in'}.first_name",
                        equalTo("George"));
    }

    @Test
    @DisplayName("Get all users email using list of strings")
    void getAllUsersEmail() {

        List<String> emails =
                given()
                        .spec(rq)
                        .when()
                        .get("/users?page=2")
                        .then()
                        .statusCode(200)
                        .extract()
                        .jsonPath()
                        .getList("data.email");

        System.out.println("List emails is - " + emails);

    }

    @Test
    @DisplayName("Getting list users with id/email params using User model(pojo class)")
    void getUsersWithSomeNeedsParams() {
        List<UserSomeInfo> users =
                given()
                        .spec(rq)
                        .when()
                        .get("/users?page=2")
                        .then()
                        .statusCode(200)
                        .extract()
                        .jsonPath()
                        // забираем лист data (юзеры по факту) и записываем в наш pojo class User
                        .getList("data", UserSomeInfo.class);

        System.out.println("Act users list is - " + users);
    }

    @Test
    @DisplayName("Getting list users with all params using UserFull model(pojo class)")
    void getUsersWithAllParams() {
        List<UserFullInfo> users =
                given()
                        .spec(rq)
                        .when()
                        .get("/users?page=2")
                        .then()
                        .statusCode(200)
                        .extract()
                        .jsonPath()
                        // забираем лист data (юзеры по факту) и записываем в наш pojo class User
                        .getList("data", UserFullInfo.class);

        //Используя assertJ проверяем что в ключах email есть искомое значение
        assertThat(users).extracting(UserFullInfo::getEmail).contains("george.edwards@reqres.in");
    }

    @Test
    @DisplayName("SHORT VERSION - Getting list users with all params" +
            " using UserFull pojo class")
    void getUsersWithAllParamsShortVersion() {
        List<UserFullInfo> users =
                UsersSteps.getUsers();

        //Используя assertJ проверяем что в ключах email есть искомое значение
        assertThat(users).extracting(UserFullInfo::getEmail)
                .contains("george.bluth@reqres.in");
    }

    @Test
    @DisplayName("Creating user")
    void createUser() {
        //add request body using lombok pattern builder
        UserRequest request =
                UserRequest.builder()
                        .name("simple")
                        .job("automation")
                        .build();


        CreateUserResponse createUserResponse = given()
                .spec(rq)
                .body(request)
                .when()
                .post("/users")
                .then()
                .spec(rs)
                .extract().as(CreateUserResponse.class);

        assertThat(createUserResponse)
                .isNotNull()
                .extracting(CreateUserResponse::getName)
                .isEqualTo(request.getName());


    }

    @Test
    @DisplayName("SHORT VERSION - Creating user using steps")
    void createUserUsingSteps() {
        UserRequest request =
                UserRequest.builder()
                        .name("simple")
                        .job("automation")
                        .build();

        UsersSteps userApi = new UsersSteps();
        CreateUserResponse response = userApi.createUser(request);

        assertThat(response)
                .isNotNull()
                .extracting(CreateUserResponse::getName)
                .isEqualTo(request.getName());
    }

    @Test
    @DisplayName("Creating new user using UserGenerator class")
    void createUserUsingUserGeneratorClass() {
        UserRequest request =
                UserGenerator.getSimpleUser();

        UsersSteps userApi = new UsersSteps();
        CreateUserResponse response = userApi.createUser(request);

        assertThat(response)
                .isNotNull()
                .extracting(CreateUserResponse::getName)
                .isEqualTo(request.getName());
    }


}
