package com.playus.twp_service.global.validation;

import com.playus.twp_service.global.Describable;
import com.playus.twp_service.party.enums.PartyAgeGroup;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.annotation.Annotation;
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
        if (Objects.isNull(value) || value.isEmpty()) {
            return false;
        }

        for (String val : value) {
            if (isNotContainedFromEnumList(val)) {
                return false; // 하나라도 description에 없으면 검증 실패
            }
        }

        return true;
    }

    private boolean isNotContainedFromEnumList(String val) {
        return !validDescriptions.contains(val);
    }
}
