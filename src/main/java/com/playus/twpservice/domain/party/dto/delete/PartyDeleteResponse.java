package com.playus.twpservice.domain.party.dto.delete;

import lombok.Builder;

@Builder
public record PartyDeleteResponse(
    Long deletedPartyId
) {
    public static PartyDeleteResponse of(Long deletedPartyId) {
        return PartyDeleteResponse.builder()
                .deletedPartyId(deletedPartyId)
                .build();
    }
}
