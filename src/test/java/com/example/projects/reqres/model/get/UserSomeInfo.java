package com.example.projects.reqres.model.get;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jdk.jfr.Description;

/**
 * Standard Deserialization process
 */

@Description("Преобразуем ответ от эндпоинта api/users?page=2 - в объект" +
        "(в нашем случае из JSON в структуру класса) " +
        "Pojo class(Модель данных) - описывает структуру данных, не несет никакой логики")
//В теле ответа указываем, что нам нужны только 2 ключа это id и email - остальные ключи проигноряться
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSomeInfo {

    // для установки значений необходимо иметь сеттеры и геттеры для получений значений из класса
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private int id;
    private String email;
}
