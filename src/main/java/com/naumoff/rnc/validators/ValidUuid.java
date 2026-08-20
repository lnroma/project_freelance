package com.naumoff.rnc.validators;

import com.naumoff.rnc.validators.validators.ValidUuidTypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {ValidUuidTypeValidator.class})
public @interface ValidUuid {
    String message() default "UUID is not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

