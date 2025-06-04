package com.playus.twpservice.domain.party.dto.participants;

import lombok.Builder;

@Builder
public record PartyParticipantsInfoResponse(
        Long userId,
        String name
) {

    public static PartyParticipantsInfoResponse of (Long userId, String name) {
        return PartyParticipantsInfoResponse.builder()
                .userId(userId)
                .name(name)
                .build();
    }
}
