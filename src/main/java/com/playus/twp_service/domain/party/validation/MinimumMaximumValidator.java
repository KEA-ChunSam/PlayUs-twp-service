package com.playus.twp_service.domain.party.validation;

import com.playus.twp_service.domain.party.dto.party_create.PartyCreateRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MinimumMaximumValidator implements ConstraintValidator<ValidMinimumMaximumParticipants, PartyCreateRequest> {

    @Override
    public boolean isValid(PartyCreateRequest request, ConstraintValidatorContext context) {
        if (request.minimumParticipants() == null || request.maximumParticipants() == null) {
            return true;
        }

        return request.minimumParticipants() <= request.maximumParticipants();
    }
}
