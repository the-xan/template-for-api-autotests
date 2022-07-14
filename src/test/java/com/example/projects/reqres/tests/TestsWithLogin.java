package com.example.projects.reqres.tests;

import com.example.projects.reqres.model.get.UserFullInfo;
import com.example.projects.reqres.model.post.CreateUserResponse;
import com.example.projects.reqres.model.post.UserRequest;
import com.example.projects.reqres.utils.RestWrapper;
import com.example.projects.reqres.utils.UserGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestsWithLogin {

    private static RestWrapper api;

    @BeforeAll
    public static void prepareClient() {
        api = RestWrapper.loginAs("eve.hotl@reqres.in", "cityslicka");
    }

    @Test
    @DisplayName("Login new user -> get users")
    void getUsersWithGetCookie() {
        assertThat(api.user.getUsers()).extracting(UserFullInfo::getEmail)
                .contains("george.bluth@reqres.in");
    }

    @Test
    @DisplayName("Creating new user using UserGenerator class")
    void createUserWithGetCookie() {
        UserRequest rq = UserGenerator.getSimpleUser();
        CreateUserResponse rs = api.user.createUser(rq);


        assertThat(rs)
                .isNotNull()
                .extracting(CreateUserResponse::getName)
                .isEqualTo(rs.getName());
    }



}
