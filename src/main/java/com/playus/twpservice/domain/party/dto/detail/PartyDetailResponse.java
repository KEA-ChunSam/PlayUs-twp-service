package com.playus.twpservice.domain.party.dto.detail;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.playus.twpservice.domain.party.enums.PartyAgeGroup;
import com.playus.twpservice.domain.party.enums.PartyGender;
import com.playus.twpservice.domain.party.enums.PartyJoinMethod;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PartyDetailResponse(
        Long partyId,
        Long writerId,
        String title,
        String partyJoinMethod,
        String text,
        List<String> partyAges,
        String availableGender,
        String authorName,
        String authorGender,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "M.d(E) a h:mm", timezone = "Asia/Seoul")
        LocalDateTime matchDate, // BE 기준 match entity에서 LocalDateTime 으로 저장

        Long currentParticipantsCount,
        Long maximumParticipantsCount,
        List<String> partyThumbnailUrls,
        List<String> userThumbnailUrls

) {

        public static PartyDetailResponse of(
                Long partyId,
                Long writerId,
                String title,
                PartyJoinMethod partyJoinMethod,
                String text,
                List<PartyAgeGroup> partyAges,
                PartyGender availableGender,
                String authorName,
                String authorGender,
                LocalDateTime matchDate,
                Long currentParticipantsCount,
                Long maximumParticipantsCount,
                List<String> partyThumbnailUrls,
                List<String> userThumbnailUrls
        ) {
                return PartyDetailResponse.builder()
                        .partyId(partyId)
                        .writerId(writerId)
                        .title(title)
                        .partyJoinMethod(partyJoinMethod.getDescription())
                        .text(text)
                        .partyAges(partyAges.stream().map(PartyAgeGroup::getDescription).toList())
                        .availableGender(availableGender.getDescription())
                        .authorName(authorName)
                        .authorGender(authorGender)
                        .matchDate(matchDate)
                        .currentParticipantsCount(currentParticipantsCount)
                        .maximumParticipantsCount(maximumParticipantsCount)
                        .partyThumbnailUrls(partyThumbnailUrls)
                        .userThumbnailUrls(userThumbnailUrls)
                        .build();
        }

}
