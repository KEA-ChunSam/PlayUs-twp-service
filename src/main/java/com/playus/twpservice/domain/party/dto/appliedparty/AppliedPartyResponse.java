package com.playus.twpservice.domain.party.dto.appliedparty;

import lombok.Builder;

import java.util.List;


@Builder
public record AppliedPartyResponse(
        Long partyId,
        String title,
        List<String> partyAgeGroup,
        String partyGender,
        String partyJoinRequestStatus,
        Long writerId,
        String authorName,
        String authorGender,
        String authorAge,
        String writerThumbnailUrl,
        int currentParticipants
) {

    public static AppliedPartyResponse of(Long partyId, String title, List<String> partyAges,
                                          String partyGender, String partyJoinRequestStatus,
                                          Long writerId, String authorName, String authorGender, String authorAge, String writerThumbnailUrl, int currentParticipants) {
        return AppliedPartyResponse.builder()
                .partyId(partyId)
                .title(title)
                .partyAgeGroup(partyAges)
                .partyGender(partyGender)
                .partyJoinRequestStatus(partyJoinRequestStatus)
                .writerId(writerId)
                .authorName(authorName)
                .authorGender(authorGender)
                .authorAge(authorAge)
                .writerThumbnailUrl(writerThumbnailUrl)
                .currentParticipants(currentParticipants)
                .build();
    }

}
