package com.playus.twpservice.domain.party.dto.end;

import lombok.Builder;

@Builder
public record PartyEndResponse(
        Long endedPartyId
) {

    public static PartyEndResponse of(Long endedPartyId) {
        return PartyEndResponse.builder()
                .endedPartyId(endedPartyId)
                .build();
    }
}
