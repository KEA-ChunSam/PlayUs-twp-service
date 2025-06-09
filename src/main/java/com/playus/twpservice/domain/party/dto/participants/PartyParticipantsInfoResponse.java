package com.playus.twpservice.domain.party.dto.participants;

import lombok.Builder;

@Builder
public record PartyParticipantsInfoResponse(
        Long userId,
        String name,
        String thumbnailUrl
) {

    public static PartyParticipantsInfoResponse of (Long userId, String name, String thumbnailUrl) {
        return PartyParticipantsInfoResponse.builder()
                .userId(userId)
                .name(name)
                .thumbnailUrl(thumbnailUrl)
                .build();
    }
}
