package com.example.projects.reqres.utils;

import com.example.projects.reqres.model.post.UserRequest;

/**
 * Generate NEW user
 */
public class UserGenerator {
    public static UserRequest getSimpleUser() {
        return UserRequest.builder()
                .name("simple")
                .job("automation")
                .build();
    }
}
