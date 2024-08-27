package com.example.socialNetwork.validators;
/*Этот валидатор помогает отловить ошибки которые будут приходить на наш сервер
        * Эти ошибки будут к нам вываливаться благодарая validation-фреймворку,
        * а именно приходят они в объекте типа BindingResult. Т.е. когда сработает валидация
        * например в классе LoginRequest то она придет именно в этом объекте*/

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.Map;

@Service
public class ResponseErrorValidator {

    public ResponseEntity<Object> mappedValidatorService(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();

            if (!CollectionUtils.isEmpty(bindingResult.getAllErrors())) {
                for (ObjectError error : bindingResult.getAllErrors()) {
                    errorMap.put(error.getCode(), error.getDefaultMessage());
                }
            }

            for (FieldError error: bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }

            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }

        return null;

    }
}
