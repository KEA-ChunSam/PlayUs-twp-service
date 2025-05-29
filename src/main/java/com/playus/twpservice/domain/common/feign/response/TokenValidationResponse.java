package com.playus.twpservice.domain.common.feign.response;

import lombok.Builder;

@Builder
public record TokenValidationResponse(
        boolean blacklisted
) {
    public static TokenValidationResponse of (Boolean blacklisted) {
        return TokenValidationResponse.builder()
                .blacklisted(blacklisted)
                .build();
    }
}
