package com.example.caloriecalculator.validation;

import org.passay.*;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        org.passay.PasswordValidator validator = new org.passay.PasswordValidator(Arrays.asList(
                new LengthRule(8,15),
                new UppercaseCharacterRule(2),
                new LowercaseCharacterRule(2),
                new DigitCharacterRule(2),
                new SpecialCharacterRule(2),
                new WhitespaceRule()

        ));
        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(validator.getMessages(result) + ",").addConstraintViolation();
        return false;
    }
}
