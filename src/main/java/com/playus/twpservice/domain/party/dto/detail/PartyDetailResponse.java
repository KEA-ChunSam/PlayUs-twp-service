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
        String authorAge,

        Long currentParticipantsCount,
        Long maximumParticipantsCount,
        Long chatRoomId,
        List<String> partyThumbnailUrls,
        List<String> userThumbnailUrls

) {

        // author, writer 관련 msa 주석 처리
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
                int authorAge,
                Long currentParticipantsCount,
                Long maximumParticipantsCount,
                Long chatRoomId,
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
//                        .authorAge(PartyAgeGroup.getAgeDescriptionByAge( (authorAge/10) * 10 ))
                        .authorAge(PartyAgeGroup.getAgeDescriptionByAge(10))
                        .currentParticipantsCount(currentParticipantsCount)
                        .maximumParticipantsCount(maximumParticipantsCount)
                        .chatRoomId(chatRoomId)
                        .partyThumbnailUrls(partyThumbnailUrls)
                        .userThumbnailUrls(userThumbnailUrls)
                        .build();
        }

}
