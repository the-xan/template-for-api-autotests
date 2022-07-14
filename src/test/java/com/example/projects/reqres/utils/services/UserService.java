package com.example.projects.reqres.utils.services;

import com.example.projects.reqres.model.get.UserFullInfo;
import com.example.projects.reqres.model.post.UserRequest;
import com.example.projects.reqres.model.post.CreateUserResponse;
import io.qameta.allure.Step;
import io.restassured.http.Cookies;

import java.util.List;

import static io.restassured.RestAssured.given;

public class UserService  extends BaseRestService{

    public UserService(Cookies cookies) {
        super(cookies);
    }

    @Override
    protected String getBasePath() {
        return "/users";
    }

    public CreateUserResponse createUser(UserRequest rq){
        return given().spec(REQ_SPEC).body(rq).post().as(CreateUserResponse.class);
    }

    @Step("Get list of users")
    public static List<UserFullInfo> getUsers(){
        return given().spec(REQ_SPEC)
                .get()
                .jsonPath().getList("data", UserFullInfo.class);
    }


}
