package com.playus.twp_service.global.validation;

import com.playus.twp_service.global.Describable;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidEnumValidator implements ConstraintValidator<ValidEnum, String> {

    private Set<String> enumValues;

    @Override
    public void initialize(ValidEnum annotation) {
        enumValues = Arrays.stream(annotation.enumClass().getEnumConstants())
                .map(Describable::getDescription)
                .collect(Collectors.toSet());
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (isInvalidEnum(value)) {
            return false;
        }

        return enumValues.contains(value);
    }

    private static boolean isInvalidEnum(String value) {
        return StringUtils.isEmpty(value);
    }
}
