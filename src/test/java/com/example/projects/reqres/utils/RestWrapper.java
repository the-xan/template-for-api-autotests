package com.example.projects.reqres.utils;

import com.example.projects.reqres.model.post.UserLogin;
import com.example.projects.reqres.utils.services.OrderService;
import com.example.projects.reqres.utils.services.UserService;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;

import static com.example.projects.reqres.utils.CustomApiListener.withCustomTemplates;
import static io.restassured.RestAssured.given;

public class RestWrapper {
    private static final String BASE_URI = "https://reqres.in/api";
    private Cookies cookies;


    public UserService user;
    public OrderService order;


    private RestWrapper(Cookies cookies){
        this.cookies = cookies;

        user = new UserService(cookies);
        order = new OrderService(cookies);
    }

    public static RestWrapper loginAs(String login, String password) {
        Cookies cookies = given()
                .filter(withCustomTemplates())
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .basePath("/login")
                .log().all()
                .body(new UserLogin(login, password))
                .post()
                .getDetailedCookies();
        return new RestWrapper(cookies);
    }
}
