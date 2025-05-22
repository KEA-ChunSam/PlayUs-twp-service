package com.playus.twpservice.domain.party.dto.cancel;

import lombok.Builder;

@Builder
public record PartyCancelResponse(
        String message
) {

    public static PartyCancelResponse of(String message) {
        return PartyCancelResponse.builder()
                .message(message)
                .build();
    }
}
