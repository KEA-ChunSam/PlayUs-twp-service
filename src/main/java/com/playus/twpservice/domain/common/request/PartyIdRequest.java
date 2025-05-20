package com.playus.twpservice.domain.common.request;

import jakarta.validation.constraints.Min;

public record PartyIdRequest(
        @Min(value = 1, message = "직관팟 ID는 1 이상이어야 합니다!")
        Long partyId
) {
    public static PartyIdRequest of(Long partyId) {
        return new PartyIdRequest(partyId);
    }
}
