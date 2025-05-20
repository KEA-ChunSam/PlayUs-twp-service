package com.playus.twpservice.domain.party.dto.applieduser;

import lombok.Builder;

@Builder
public record PartyAppliedUserResponse(
        Long userId,
        String name,
        String ageGroup,
        String thumbnailImageUrl,
        String requireMessage
) {

    public static PartyAppliedUserResponse of(Long userId, String name, String ageGroup, String thumbnailImageUrl, String requireMessage) {
        return PartyAppliedUserResponse.builder()
                .userId(userId)
                .name(name)
                .ageGroup(ageGroup)
                .thumbnailImageUrl(thumbnailImageUrl)
                .requireMessage(requireMessage)
                .build();
    }
}
