package com.example.projects.reqres.utils.services;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;

import static com.example.projects.reqres.utils.CustomApiListener.withCustomTemplates;
import static io.restassured.filter.log.LogDetail.ALL;

public abstract class BaseRestService {
    private static final String BASE_URI = "https://reqres.in/api";
    protected Cookies cookies;
    protected static RequestSpecification REQ_SPEC;

    protected abstract String getBasePath();

    public BaseRestService(Cookies cookies) {
        this.cookies = cookies;

        REQ_SPEC =
                new RequestSpecBuilder()
                        .addCookies(cookies)
                        .addFilter(withCustomTemplates())
                        .log(ALL)
                        .setBaseUri("https://reqres.in/api")
                        .setBasePath("/users?page=2")
                        .setContentType(ContentType.JSON)
                        .build();
    }
}
