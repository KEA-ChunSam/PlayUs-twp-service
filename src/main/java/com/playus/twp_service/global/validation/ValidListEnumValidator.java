package com.playus.twp_service.global.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidListEnumValidator implements ConstraintValidator<ValidEnumList, String> {

    private Set<String> enumValues;

    @Override
    public void initialize(ValidEnumList annotation) {
        enumValues = Arrays.stream(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        // 필드가 null, 비어 있거나 list 중 하나 이상의 값이 enum에 속해 잇지 않는 경우
        if (value == null || value.isEmpty()) {
            return true;
        }
        // 필드의 값이 Enum 값들에 포함되어 있는지 확인
        return enumValues.contains(value);
    }
}
