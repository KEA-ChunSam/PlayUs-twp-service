package com.playus.twpservice.domain.party.dto.approve;

import lombok.Builder;

@Builder
public record PartyApproveResponse(
        String message
) {
    public static PartyApproveResponse of(String message) {
        return PartyApproveResponse.builder()
                .message(message)
                .build();
    }
}
