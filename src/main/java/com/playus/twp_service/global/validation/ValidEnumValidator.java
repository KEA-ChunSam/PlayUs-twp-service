package com.playus.twp_service.global.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidEnumValidator implements ConstraintValidator<ValidEnum, String> {

    private Set<String> enumValues;

    // enum의 값을 가져와 enumValues 에 저장
    @Override
    public void initialize(ValidEnum annotation) {
        enumValues = Arrays.stream(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

    // enumValues 를 통한 실제 validation 로직 수행
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (isInvalidEnum(value)) {
            return true;
        }

        // 필드의 값이 Enum 값들에 포함되어 있는지 확인
        return enumValues.contains(value);
    }

    private static boolean isInvalidEnum(String value) {
        return value == null || value.isEmpty();
    }
}
