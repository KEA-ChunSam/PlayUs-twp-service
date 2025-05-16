package com.playus.twpservice.domain.party.validation;

import com.playus.twpservice.domain.common.MinimumMaximumValidatable;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MinimumMaximumValidator implements ConstraintValidator<ValidMinimumMaximumParticipants, MinimumMaximumValidatable> {

    @Override
    public boolean isValid(MinimumMaximumValidatable request, ConstraintValidatorContext context) {
        if (request.minimumParticipants() == null || request.maximumParticipants() == null) {
            return true;
        }

        return request.minimumParticipants() <= request.maximumParticipants();
    }
}
