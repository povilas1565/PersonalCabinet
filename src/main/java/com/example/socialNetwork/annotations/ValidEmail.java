package com.example.socialNetwork.annotations;

import com.example.socialNetwork.validators.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/* Давайте проверим адрес электронной почты и убедимся, что он правильно сформирован.
Мы собираемся создать для этого кастомный валидатор, а также кастомную аннотацию валидации - назовем это @ValidEmail.

Небольшое примечание: мы запускаем нашу собственную аннотацию вместо @Email Hibernate, потому что Hibernate считает допустимым
старый формат адресов интернете: myaddress@myserver
(см. Статью Stackoverflow https://stackoverflow.com/questions/4459474/hibernate-validator-email-accepts-askstackoverflow-as-valid), что не очень хорошо.
Вот аннотация для проверки электронной почты и настраиваемый валидатор:*/
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
@Documented
public @interface ValidEmail {

    String message() default "Invalid email";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
