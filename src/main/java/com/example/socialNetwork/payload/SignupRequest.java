package com.example.socialNetwork.payload;

/*Объект этого класса мы будем передавать на сервер при попытке зарегистрировать нового пользователя*/
/*Далее - давайте посмотрим на проверки, которые контроллер будет выполнять при регистрации новой учетной записи:
Все обязательные поля заполнены (нет пустых полей)
Электронный адрес действителен (правильно сформирован)
Поле подтверждения пароля совпадает с полем пароля
Аккаунт еще не существует*/

import com.example.socialNetwork.annotations.PasswordMatches;
import com.example.socialNetwork.annotations.ValidEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
public class SignupRequest {

    @Email(message = "Нужен формат почты")
    @NotBlank(message = "Почта юзера необходима")
    private String email;

    @NotEmpty(message = "Введите свое имя")
    private String firstName;

    @NotEmpty(message = "Введите свою фамилию")
    private String lastName;

    @NotEmpty(message = "Введите никнейм")
    private String userName;

    @NotEmpty(message = "Введите пароль")
    @Size(min = 5, max = 30)
    private String password;

    @NotEmpty(message =  "Подтвердите пароль")
    private String confirmPassword;

}
