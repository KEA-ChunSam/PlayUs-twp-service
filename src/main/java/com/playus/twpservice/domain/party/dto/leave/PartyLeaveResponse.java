package com.playus.twpservice.domain.party.dto.leave;

import lombok.Builder;

@Builder
public record PartyLeaveResponse(
        String message
) {
    public static PartyLeaveResponse of (String message) {
        return PartyLeaveResponse.builder()
                .message(message)
                .build();
    }
}
