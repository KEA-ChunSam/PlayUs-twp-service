package com.playus.twpservice.global.validation;

import com.playus.twpservice.global.Describable;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
  * Enum 값을 검증하기 위한 애노테이션입니다.
 * Describable 인터페이스를 구현한 Enum 클래스에 대해 문자열 값이 유효한 Enum 값인지 검증합니다.
 *
 * @see Describable
 * @see ValidEnumValidator
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidEnumValidator.class)
public @interface ValidEnum {

    String message() default "";
    String emptyValueMessage();
    String invalidValueMessage();
    Class<?>[] groups() default {};
    Class<? extends Payload> [] payload() default {};

    Class<? extends Describable> enumClass();
}
