package com.playus.twpservice.domain.common.feign.response;

import lombok.Builder;

@Builder
public record PartyParticipantsInfoFeignResponse(
        Long userId,
        String name,
        int age,
        String thumbnailUrl
) {

    public static PartyParticipantsInfoFeignResponse of(Long userId, String name, int age, String thumbnailUrl) {
        return PartyParticipantsInfoFeignResponse.builder()
                .userId(userId)
                .name(name)
                .age(age)
                .thumbnailUrl(thumbnailUrl)
                .build();
    }

    public static PartyParticipantsInfoFeignResponse withServiceUnavailable() {
        return PartyParticipantsInfoFeignResponse.builder()
                .name("이름을 불러올 수 없습니다.")
                .age(0)
                .thumbnailUrl("")
                .build();
    }
}
