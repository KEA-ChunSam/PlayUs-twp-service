package com.playus.twpservice.domain.party.dto.info;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.playus.twpservice.domain.party.enums.PartyAgeGroup;
import com.playus.twpservice.domain.party.enums.PartyGender;
import com.playus.twpservice.domain.party.enums.PartyJoinMethod;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PartyInfoResponse(
        Long partyId,
        Long writerId,
        String title,
        String partyJoinMethod,
        List<String> partyAges,
        String availableGender,
        String authorName,
        String authorGender,
        String authorAge,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "M.d(E) a h:mm", timezone = "Asia/Seoul")
        LocalDateTime matchDate, // BE 기준 match entity에서 LocalDateTime 으로 저장

        Long currentParticipantsCount,
        Long maximumParticipantsCount,
        List<String> partyThumbnailUrls,
        List<String> userThumbnailUrls
) {

    // author, writer 관련 msa 주석 처리
    public static PartyInfoResponse of(
            Long partyId,
            Long writerId,
            String title,
            PartyJoinMethod partyJoinMethod,
            List<PartyAgeGroup> partyAges,
            PartyGender availableGender,
            String authorName,
            String authorGender,
            int authorAge,
            LocalDateTime matchDate,
            Long currentParticipantsCount,
            Long maximumParticipantsCount,
            List<String> partyThumbnailUrls,
            List<String> userThumbnailUrls
    ) {
        return PartyInfoResponse.builder()
                .partyId(partyId)
                .writerId(writerId)
                .title(title)
                .partyJoinMethod(partyJoinMethod.getDescription())
                .partyAges(partyAges.stream().map(PartyAgeGroup::getDescription).toList())
                .availableGender(availableGender.getDescription())
                .authorName(authorName)
                .authorGender(authorGender)
//                .authorAge(PartyAgeGroup.getAgeDescriptionByAge((authorAge / 10) * 10))
                .authorAge(PartyAgeGroup.getAgeDescriptionByAge(10)) // 임시
                .matchDate(matchDate)
                .currentParticipantsCount(currentParticipantsCount)
                .maximumParticipantsCount(maximumParticipantsCount)
                .partyThumbnailUrls(partyThumbnailUrls)
                .userThumbnailUrls(userThumbnailUrls)
                .build();
    }
}
