package com.example.tests.reqres.model.get;

import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.jfr.Description;
import lombok.Data;

/**
 * Deserialization process using lombok
 */
@Description("Преобразуем ответ от эндпоинта api/users?page=2 - в объект")
//перед компиляцией благодаря аннотации @Data lombok сгенирирует автоматом сеттеры и геттеры
@Data
public class UserFullInfo {

    private int id;
    private String email;
    /*
    Значение переменной должно соответвовать ключу в ответе, но из-за синтаксиса java
    нужно прорисать аннотацию @JsonProperty в которой укажем корректное значение ключа
     */
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String avatar;

}
