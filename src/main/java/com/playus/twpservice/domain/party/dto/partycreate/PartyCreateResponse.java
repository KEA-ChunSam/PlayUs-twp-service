package com.playus.twpservice.domain.party.dto.partycreate;

import lombok.Builder;

@Builder
public record PartyCreateResponse (
      Long partyId,
      String chatRoomId
) {

    public static PartyCreateResponse of(Long partyId, String chatRoomId) {
        return PartyCreateResponse.builder()
                .partyId(partyId)
                .chatRoomId(chatRoomId)
                .build();
    }
}
