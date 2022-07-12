package com.example.tests.reqres.model.post;

import com.example.helpers.DateDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateUserResponse {
    private String name;
    private String job;
    private int id;
    //для указания необходимого формата нужно использовать аннотацию @JsonFormat
    //"yyyy-MM-dd'T'hh:mm:ss.SSSZ" - стандарт для java time format
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss.SSSZ")
    @JsonDeserialize(using = DateDeserializer.class)
    private LocalDateTime createdAt;
}
