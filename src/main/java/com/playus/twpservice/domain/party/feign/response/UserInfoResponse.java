package com.playus.twpservice.domain.party.feign.response;

import lombok.Builder;

@Builder
public record UserInfoResponse(
        String nickname,
        String profileImageUrl
) {

    public static UserInfoResponse withServiceUnavailable() {
        return UserInfoResponse.builder()
                .nickname(null)
                .profileImageUrl(null)
                .build();
    }
}
