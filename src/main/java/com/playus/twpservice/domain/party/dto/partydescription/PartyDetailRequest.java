package com.playus.twpservice.domain.party.dto.partydescription;

import jakarta.validation.constraints.Min;

public record PartyDetailRequest(
        @Min(value = 1, message = "직관팟 ID는 1 이상이어야 합니다!")
        Long partyId
) {

    public static PartyDetailRequest of(Long partyId) {
        return new PartyDetailRequest(partyId);
    }

}
