package com.playus.twp_service.global.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidEnumValidator.class)
public @interface ValidEnum {

    String message();
    Class<?>[] groups() default {};
    Class<? extends Payload> [] payload() default {};

    Class<? extends Enum<?>> enumClass();
}
