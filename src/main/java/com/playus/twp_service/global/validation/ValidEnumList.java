package com.playus.twp_service.global.validation;

import com.playus.twp_service.global.Describable;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidEnumListValidator.class)
public @interface ValidEnumList {

    String message() default "";
    String emptyValueMessage();
    String invalidValueMessage();
    String overValueMessage();

    Class<?>[] groups() default {};
    Class<? extends Payload> [] payload() default {};

    Class<? extends Describable> enumClass();
}
