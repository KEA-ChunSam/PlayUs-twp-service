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
    private String emptyMessage;
    private String notFoundMeessage;

    @Override
    public void initialize(ValidEnum annotation) {
        emptyMessage = annotation.emptyMessage();
        notFoundMeessage = annotation.notFoundMessage();
        enumValues = Arrays.stream(annotation.enumClass().getEnumConstants())
                .map(Describable::getDescription)
                .collect(Collectors.toSet());
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (isEmptyValue(value)) {
            setCustomMessageInValidationContext(context, emptyMessage);
            return false;
        }

        if (isNotExistValueInEnum(value)) {
            setCustomMessageInValidationContext(context, notFoundMeessage);
            return false;
        }

        return enumValues.contains(value);
    }

    private void setCustomMessageInValidationContext(ConstraintValidatorContext context, String validationMessage) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(validationMessage)
                .addConstraintViolation();
    }

    private boolean isNotExistValueInEnum(String value) {
        return !enumValues.contains(value);
    }

    private static boolean isEmptyValue(String value) {
        return StringUtils.isEmpty(value);
    }
}
