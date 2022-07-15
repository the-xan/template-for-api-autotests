package com.example.projects.reqres.model.post;

import com.example.projects.reqres.utils.DateDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper=true)
public class CreateUserResponse extends UserRequest{
    private int id;
    //для указания необходимого формата нужно использовать аннотацию @JsonFormat
    //"yyyy-MM-dd'T'hh:mm:ss.SSSZ" - стандарт для java time format
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss.SSSZ")
    @JsonDeserialize(using = DateDeserializer.class)
    private LocalDateTime createdAt;
}
