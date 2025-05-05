package com.playus.twp_service.domain.party.dto.party_create;

import lombok.Builder;

@Builder
public record PartyCreateResponse (
      Long partyId,
      Boolean success
) {

    public static PartyCreateResponse of(Long partyId, Boolean success) {
        return PartyCreateResponse.builder()
                .partyId(partyId)
                .success(success)
                .build();
    }
}
