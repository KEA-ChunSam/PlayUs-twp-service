package com.playus.twp_service.party.dto.party_create;

import com.playus.twp_service.global.validation.ValidEnum;
import com.playus.twp_service.global.validation.ValidEnumList;
import com.playus.twp_service.party.enums.Method;
import com.playus.twp_service.party.enums.PartyAgeGroup;
import com.playus.twp_service.party.enums.PartyGender;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;

@Builder
public record PartyCreateRequest(

        @NotBlank(message = "직관팟 제목이 비어 있습니다!")
        String title,

        @ValidEnum(enumClass = Method.class, message = "신청 방식이 비어 있습니다!")
        String method,

        @ValidEnum(enumClass = PartyGender.class, message = "참여 원하는 성별이 비어 있습니다!")
        String gender,

        @ValidEnumList(enumClass = PartyAgeGroup.class, message = "참여자 나이가 비어 있습니다!")
        List<String> ageGroup,

        @NotBlank(message = "직관팟 소개 문구가 비어 있습니다!")
        String message
) {

    public static PartyCreateRequest of(String title, String method, String gender, List<String> ageGroup, String message) {
        return PartyCreateRequest.builder()
                .title(title)
                .method(method)
                .gender(gender)
                .ageGroup(ageGroup)
                .message(message)
                .build();
    }
}
