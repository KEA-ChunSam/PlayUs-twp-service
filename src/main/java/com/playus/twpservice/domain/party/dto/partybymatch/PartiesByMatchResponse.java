package com.playus.twpservice.domain.party.dto.partybymatch;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PartiesByMatchResponse(
        String title,
        String partyJoinMethod,
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


    public static PartiesByMatchResponse of(
            String title,
            String partyJoinMethod,
            List<String> partyAges,
            String availableGender,
            String authorName,
            String authorGender,
            LocalDateTime matchDate,
            Long currentParticipantsCount,
            Long maximumParticipantsCount,
            List<String> partyThumbnailUrls,
            List<String> userThumbnailUrls
    ) {
        return PartiesByMatchResponse.builder()
                .title(title)
                .partyJoinMethod(partyJoinMethod)
                .partyAges(partyAges)
                .availableGender(availableGender)
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
