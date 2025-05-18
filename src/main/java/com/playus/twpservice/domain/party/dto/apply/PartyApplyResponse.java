package com.playus.twpservice.domain.party.dto.apply;

import lombok.Builder;

@Builder
public record PartyApplyResponse(
        String message
) {

    public static PartyApplyResponse of(String message) {
        return PartyApplyResponse.builder()
                .message(message)
                .build();
    }
}
