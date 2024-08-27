package com.example.socialNetwork.validators;

import com.example.socialNetwork.annotations.PasswordMatches;
import com.example.socialNetwork.payload.SignupRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {}

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        SignupRequest signupRequest = (SignupRequest) o;
        return signupRequest.getPassword().equals(signupRequest.getConfirmPassword());

    }
}
