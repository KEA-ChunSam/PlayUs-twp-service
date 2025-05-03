package com.playus.twp_service.party.dto.party_create;

import com.playus.twp_service.global.validation.ValidEnum;
import com.playus.twp_service.global.validation.ValidEnumList;
import com.playus.twp_service.party.entity.Party;
import com.playus.twp_service.party.enums.PartyJoinMethod;
import com.playus.twp_service.party.enums.PartyAgeGroup;
import com.playus.twp_service.party.enums.PartyGender;
import com.playus.twp_service.party.validation.ValidMinimumMaximumParticipants;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;


@Builder
@ValidMinimumMaximumParticipants(message = "최소 참여 인원은 최대 참여 인원보다 클 수 없습니다!")
public record PartyCreateRequest(

        @NotBlank(message = "직관팟 제목이 비어 있습니다!")
        String title,

        @ValidEnum(enumClass = PartyJoinMethod.class, emptyValueMessage = "신청 방식이 비어 있습니다!", invalidValueMessage = "잘못된 신청 방식입니다!")
        String method,

        @ValidEnum(enumClass = PartyGender.class, emptyValueMessage = "참여 원하는 성별이 비어 있습니다!", invalidValueMessage = "잘못된 성별 형식입니다!")
        String gender,

        @ValidEnumList(enumClass = PartyAgeGroup.class, emptyValueMessage = "참여자 나이가 비어 있습니다!",
                       invalidValueMessage = "잘못된 참여자 나이입니다!", overValueMessage = "참여자 나이는 최대 6개까지 가능합니다!")
        List<String> ageGroup,

        @NotNull(message = "최소 참여 인원이 비어 있습니다!")
        @Min(value = 1, message = "최소 참여 인원은 1명 이상이여야 합니다!")
        Long minimumParticipants,

        @NotNull(message = "최대 참여 인원이 비어 있습니다!")
        @Min(value = 1, message = "최대 참여 인원은 1명 이상이여야 합니다!")
        Long maximumParticipants,

        @NotBlank(message = "사진 URL이 비어 있습니다!")
        String thumbnailUrl,

        @NotBlank(message = "직관팟 소개 문구가 비어 있습니다!")
        String message

) {

    public static PartyCreateRequest of(String title, String method, String gender, List<String> ageGroup,
                                        Long minimumParticipants, Long maximumParticipants,
                                        String thumbnailUrl, String message) {

        return PartyCreateRequest.builder()
                .title(title)
                .method(method)
                .gender(gender)
                .ageGroup(ageGroup)
                .minimumParticipants(minimumParticipants)
                .maximumParticipants(maximumParticipants)
                .thumbnailUrl(thumbnailUrl)
                .message(message)
                .build();
    }

    public Party toParty() {
        return Party.create(title, message, minimumParticipants, maximumParticipants, thumbnailUrl, PartyGender.toEnumValue(gender), PartyJoinMethod.toEnumValue(method));
    }

}
