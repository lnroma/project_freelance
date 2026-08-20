package com.naumoff.rnc.validators.validators;

import com.naumoff.rnc.validators.ValidUuid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.UUID;

public class ValidUuidTypeValidator
        implements ConstraintValidator<ValidUuid, String> {

    @Override
    public void initialize(ValidUuid constraintAnnotation) {
        // Инициализация не требуется
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            UUID.fromString(value);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
