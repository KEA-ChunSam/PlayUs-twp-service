package com.playus.twp_service.global.validation;

import com.playus.twp_service.global.Describable;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.*;
import java.util.stream.Collectors;

public class ValidListEnumValidator implements ConstraintValidator<ValidEnumList, List<String>> {

    private Set<String> validDescriptions;

    @Override
    public void initialize(ValidEnumList annotation) {
        validDescriptions = Arrays.stream(annotation.enumClass().getEnumConstants())
                .map(Describable::getDescription)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(List<String> value, ConstraintValidatorContext context) {
        if (Objects.isNull(value) || value.isEmpty() || value.size() > validDescriptions.size()) {
            return false;
        }

        for (String val : value) {
            if (isNotContainedFromEnumList(val)) {
                return false;
            }
        }

        return true;
    }

    private boolean isNotContainedFromEnumList(String val) {
        return !validDescriptions.contains(val);
    }
}
