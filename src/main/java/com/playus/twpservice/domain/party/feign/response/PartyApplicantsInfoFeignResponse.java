package com.playus.twpservice.domain.party.feign.response;

import lombok.Builder;

@Builder
public record PartyApplicantsInfoFeignResponse(
        String name,
        int age,
        String thumbnailUrl
) {

    public static PartyApplicantsInfoFeignResponse of(String name, int age, String thumbnailUrl) {
        return PartyApplicantsInfoFeignResponse.builder()
                .name(name)
                .age(age)
                .thumbnailUrl(thumbnailUrl)
                .build();
    }

    public static PartyApplicantsInfoFeignResponse withServiceUnavailable() {
        return PartyApplicantsInfoFeignResponse.builder()
                .name("이름을 불러올 수 없습니다.")
                .age(0)
                .thumbnailUrl("")
                .build();
    }
}
