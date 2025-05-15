package com.playus.twpservice.domain.party.dto.partyupdate;

import com.playus.twpservice.domain.common.MinimumMaximumValidatable;
import com.playus.twpservice.domain.party.entity.Party;
import com.playus.twpservice.domain.party.enums.PartyAgeGroup;
import com.playus.twpservice.domain.party.enums.PartyGender;
import com.playus.twpservice.domain.party.enums.PartyJoinMethod;
import com.playus.twpservice.domain.party.validation.ValidMinimumMaximumParticipants;
import com.playus.twpservice.global.validation.ValidEnum;
import com.playus.twpservice.global.validation.ValidEnumList;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.util.List;

@Builder
@ValidMinimumMaximumParticipants(message = "최소 참여 인원은 최대 참여 인원보다 클 수 없습니다!")
public record PartyUpdateRequest  (

        @NotBlank(message = "직관팟 제목이 비어 있습니다!")
        @Size(max = 225, message = "제목의 길이를 1~225자 이내로 작성해 주세요!")
        String title,

        @ValidEnum(enumClass = PartyJoinMethod.class, emptyValueMessage = "신청 방식이 비어 있습니다!", invalidValueMessage = "잘못된 신청 방식입니다!")
        String partyJoinMethod,

        @ValidEnum(enumClass = PartyGender.class, emptyValueMessage = "참여 원하는 성별이 비어 있습니다!", invalidValueMessage = "잘못된 성별 형식입니다!")
        String partyGender,

        @ValidEnumList(enumClass = PartyAgeGroup.class, emptyValueMessage = "참여자 나이가 비어 있습니다!",
                invalidValueMessage = "잘못된 참여자 나이입니다!", overValueMessage = "참여자 나이는 최대 6개까지 가능합니다!")
        List<String> ageGroup,

        @NotNull(message = "최소 참여 인원이 비어 있습니다!")
        @Min(value = 1, message = "최소 참여 인원은 1명 이상이여야 합니다!")
        Long minimumParticipants,

        @NotNull(message = "최대 참여 인원이 비어 있습니다!")
        @Min(value = 1, message = "최대 참여 인원은 1명 이상이여야 합니다!")
        Long maximumParticipants,

        @Size(max = 10, message = "썸네일은 최대 10개까지만 가능합니다!")
        List<
                @NotBlank(message = "사진 URL이 비어 있습니다!")
                @Pattern(regexp = "^(https?|ftp)://.*$", message = "올바른 URL 형식이 아닙니다!")
                        String>
                thumbnailUrl,

        @NotBlank(message = "직관팟 소개 문구가 비어 있습니다!")
        @Size(max = 100, message = "직관팟 소개 문구의 길이를 1~100자 이내로 작성해 주세요!")
        String message

) implements MinimumMaximumValidatable {

    public static PartyUpdateRequest of(String title, String method, String gender, List<String> ageGroup,
                                        Long minimumParticipants, Long maximumParticipants,
                                        List<String> thumbnailUrl, String message) {

        return PartyUpdateRequest.builder()
                .title(title)
                .partyJoinMethod(method)
                .partyGender(gender)
                .ageGroup(ageGroup)
                .minimumParticipants(minimumParticipants)
                .maximumParticipants(maximumParticipants)
                .thumbnailUrl(thumbnailUrl)
                .message(message)
                .build();
    }

    public Party toParty(Long partyId) {
        return Party.createForUpdateParty(partyId, title, message, minimumParticipants,
                maximumParticipants, PartyGender.toEnumValue(partyGender), PartyJoinMethod.toEnumValue(partyJoinMethod));
    }

}
