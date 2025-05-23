package com.playus.twpservice.domain.party.dto.create;

import lombok.Builder;

@Builder
public record PartyCreateResponse (
      Long partyId,
      Long chatRoomId
) {

    public static PartyCreateResponse of(Long partyId, Long chatRoomId) {
        return PartyCreateResponse.builder()
                .partyId(partyId)
                .chatRoomId(chatRoomId)
                .build();
    }
}
