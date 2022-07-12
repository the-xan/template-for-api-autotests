package com.example.tests.reqres;

import com.example.tests.reqres.model.get.UserSomeInfo;
import com.example.tests.reqres.model.get.UserFullInfo;
import com.example.tests.reqres.model.post.CreateUserRequest;
import com.example.tests.reqres.model.post.CreateUserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.example.tests.reqres.spec.Specs.requestSpecification;
import static com.example.tests.reqres.spec.Specs.responseSpecification;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@Tag("Reqres.in")
public class ReqresInTests {

    @Test
    @DisplayName("Simple logging test")
    void loginTest() {

        String body = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\" }";

        given()
                .spec(requestSpecification)
                .body(body)
        .when()
                .post("/login")
        .then()
                .spec(responseSpecification)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    @DisplayName("Simple negative logging test")
    void missingPasswordLoginTest() {

        String body = "{ \"email\": \"eve.holt@reqres.in\"}";

        given()
                .spec(requestSpecification)
                .body(body)
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
    void getUsers(){
        given()
                .spec(requestSpecification)
        .when()
                .get("/users?page=2")
        .then()
                .statusCode(200)
                .body("data.find{it.email=='george.edwards@reqres.in'}.first_name",
                        equalTo("George"));
    }

    @Test
    @DisplayName("Get all users email using list of strings")
    void getAllUsersEmail(){

        List<String> emails =
        given()
                .spec(requestSpecification)
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
    void getUsersWithSomeNeedsParams(){
        List<UserSomeInfo> users =
        given()
                .spec(requestSpecification)
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
    void getUsersWithAllParams(){
        List<UserFullInfo> users =
        given()
                .spec(requestSpecification)
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
    @DisplayName("Creating user")
    void createUser(){
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setName("simple");
        createUserRequest.setJob("automation");


        CreateUserResponse createUserResponse = given()
                .spec(requestSpecification)
                .body(createUserRequest)
        .when()
                .post("/users")
        .then()
                .extract().as(CreateUserResponse.class);

        assertThat(createUserResponse)
                .isNotNull()
                .extracting(CreateUserResponse::getName)
                .isEqualTo(createUserRequest.getName());


    }
}
