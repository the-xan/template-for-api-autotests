package com.example.projects.reqres.steps;

import com.example.projects.reqres.model.get.UserFullInfo;
import com.example.projects.reqres.model.post.UserRequest;
import com.example.projects.reqres.model.post.CreateUserResponse;
import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.List;

import static com.example.projects.reqres.utils.CustomApiListener.withCustomTemplates;
import static io.restassured.RestAssured.given;
import static io.restassured.filter.log.LogDetail.ALL;


/**
 * В steps хранятся  методы, которые описывают "бизнес действия"
 */
public class UsersSteps {

    private static final RequestSpecification REQ_SPEC =
            new RequestSpecBuilder()
                    .addFilter(withCustomTemplates())
                    .log(ALL)
                    .setBaseUri("https://reqres.in/api")
                    .setBasePath("/users?page=2")
                    .setContentType(ContentType.JSON)
                    .build();


    private CreateUserResponse user;

    @Step("Create new user")
    public CreateUserResponse createUser(UserRequest rq) {
        user = given().spec(REQ_SPEC)
                .body(rq)
                .post().as(CreateUserResponse.class);
        return user;
    }

    public UserFullInfo getUser() {
        return given().spec(REQ_SPEC).get("/" + user.getId()).as(UserFullInfo.class);
    }

    @Step("Get list of users")
    public static List<UserFullInfo> getUsers() {
        return given().spec(REQ_SPEC)
                .get()
                .jsonPath().getList("data", UserFullInfo.class);
    }


    public static UserFullInfo getUser(int id) {
        return given().spec(REQ_SPEC).get("/" + id).as(UserFullInfo.class);
    }
}
