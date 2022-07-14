package com.example.projects.reqres.utils.services;

import com.example.projects.reqres.model.get.UserFullInfo;
import io.qameta.allure.Step;
import io.restassured.http.Cookies;

import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderService extends BaseRestService{

    public OrderService(Cookies cookies) {
        super(cookies);
    }

    @Override
    protected String getBasePath() {
        return "/orders";
    }

    @Step("Get list of users")
    public static List<UserFullInfo> getOrders(){
        return given().spec(REQ_SPEC)
                .get()
                .jsonPath().getList("data", UserFullInfo.class);
    }
}
