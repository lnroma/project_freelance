package com.naumoff.rnc.validators.validators;

import com.naumoff.rnc.dto.CreateWalletRequest;
import com.naumoff.rnc.validators.ValidEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidTransactionTypeValidator
        implements ConstraintValidator<ValidEnum, String> {

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        // Инициализация не требуется
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        try {
            CreateWalletRequest.TransactionType.valueOf(value);

            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
