package com.playus.twpservice.domain.common.feign.response;

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

    public static UserInfoResponse createForTest(String nickname, String profileImageUrl) {
        return UserInfoResponse.builder()
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .build();
    }
}
