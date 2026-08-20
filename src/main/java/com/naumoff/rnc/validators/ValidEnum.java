package com.naumoff.rnc.validators;

import com.naumoff.rnc.validators.validators.ValidTransactionTypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {ValidTransactionTypeValidator.class})
public @interface ValidEnum {
    String message() default "Тип транзакции должен быть либо DEPOSIT, либо WITHDRAW";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
