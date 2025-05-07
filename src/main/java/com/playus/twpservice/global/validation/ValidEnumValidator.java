package com.playus.twpservice.global.validation;

import com.playus.twpservice.global.Describable;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidEnumValidator implements ConstraintValidator<ValidEnum, String> {


    private Set<String> enumValues;
    private String emptyValueMessage;
    private String invalidValueMessage;

    @Override
    public void initialize(ValidEnum annotation) {
        emptyValueMessage = annotation.emptyValueMessage();
        invalidValueMessage = annotation.invalidValueMessage();
        enumValues = Arrays.stream(annotation.enumClass().getEnumConstants())
                .map(Describable::getDescription)
                .collect(Collectors.toSet());
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (isEmptyValue(value)) {
            setCustomMessageInValidationContext(context, emptyValueMessage);
            return false;
        }

        if (isInvalidValue(value)) {
            setCustomMessageInValidationContext(context, invalidValueMessage);
            return false;
        }

        return enumValues.contains(value);
    }

    private void setCustomMessageInValidationContext(ConstraintValidatorContext context, String validationMessage) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(validationMessage)
                .addConstraintViolation();
    }

    private boolean isInvalidValue(String value) {
        return !enumValues.contains(value);
    }

    private static boolean isEmptyValue(String value) {
        return StringUtils.isEmpty(value);
    }
}
