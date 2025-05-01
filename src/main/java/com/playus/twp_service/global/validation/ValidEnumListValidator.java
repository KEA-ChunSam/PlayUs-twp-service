package com.playus.twp_service.global.validation;

import com.playus.twp_service.global.Describable;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.*;
import java.util.stream.Collectors;

public class ValidEnumListValidator implements ConstraintValidator<ValidEnumList, List<String>> {

    private Set<String> validDescriptions;
    private String emptyValueMessage;
    private String invalidValueMessage;
    private String overValueMessage;

    @Override
    public void initialize(ValidEnumList annotation) {
        emptyValueMessage = annotation.emptyValueMessage();
        invalidValueMessage = annotation.invalidValueMessage();
        overValueMessage = annotation.overValueMessage();

        validDescriptions = Arrays.stream(annotation.enumClass().getEnumConstants())
                .map(Describable::getDescription)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(List<String> valueList, ConstraintValidatorContext context) {
        if (isEmptyList(valueList)) {
            setCustomMessageInValidationContext(context, emptyValueMessage);
            return false;
        }

        if (isOverList(valueList)) {
            setCustomMessageInValidationContext(context, overValueMessage);
            return false;
        }

        if (invalidValueExistsInList(valueList)) {
            setCustomMessageInValidationContext(context, invalidValueMessage);
            return false;
        }

        return true;
    }

    private void setCustomMessageInValidationContext(ConstraintValidatorContext context, String validationMessage) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(validationMessage)
                .addConstraintViolation();
    }

    private boolean invalidValueExistsInList(List<String> valueList) {
        return valueList.stream().anyMatch(this::isNotContainedFromEnumList);
    }

    private boolean isOverList(List<String> valueList) {
        return valueList.size() > validDescriptions.size();
    }

    private static boolean isEmptyList(List<String> valueList) {
        return Objects.isNull(valueList) || valueList.isEmpty();
    }

    private boolean isNotContainedFromEnumList(String val) {
        return !validDescriptions.contains(val);
    }
}
