package com.example.caloriecalculator.validation;

import com.example.caloriecalculator.dto.RegistrationDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return ((RegistrationDTO) o).getPassword().equals(((RegistrationDTO) o).getConfirmPassword());
    }
}
