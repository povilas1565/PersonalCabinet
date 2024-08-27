package com.example.socialNetwork.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/*Объект этого класса мы будем передавать на сервер при попытке авторизоваться*/
@Data
public class LoginRequest {

    @NotEmpty(message = "Никнейм не пустой")
    private String userName;

    @NotEmpty(message = "Пароль не пустой")
    private String password;


}
