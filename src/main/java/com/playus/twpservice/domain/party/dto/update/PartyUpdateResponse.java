package com.playus.twpservice.domain.party.dto.update;

import lombok.Builder;

@Builder
public record PartyUpdateResponse(
        Long partyId
) {
    public static PartyUpdateResponse of(Long partyId) {
        return PartyUpdateResponse.builder()
                .partyId(partyId)
                .build();
    }
}
