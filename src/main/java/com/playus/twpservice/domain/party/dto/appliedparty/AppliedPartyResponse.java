package com.playus.twpservice.domain.party.dto.appliedparty;

import com.playus.twpservice.domain.party.enums.PartyAgeGroup;
import com.playus.twpservice.domain.party.enums.PartyGender;
import com.playus.twpservice.domain.party.enums.PartyJoinRequestStatus;
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

    public static AppliedPartyResponse of(Long partyId, String title, List<Integer> partyAges,
                                          PartyGender partyGender, PartyJoinRequestStatus partyJoinRequestStatus,
                                          Long writerId, String authorName, String authorGender, int authorAge, String writerThumbnailUrl, int currentParticipants) {
        return AppliedPartyResponse.builder()
                .partyId(partyId)
                .title(title)
                .partyAgeGroup(partyAges.stream().map(PartyAgeGroup::getAgeDescriptionByAge).toList())
                .partyGender(partyGender.getDescription())
                .partyJoinRequestStatus(partyJoinRequestStatus.getMessage())
                .writerId(writerId)
                .authorName(authorName)
                .authorGender(authorGender)
                .authorAge(PartyAgeGroup.getAgeDescriptionByAge(authorAge))
                .writerThumbnailUrl(writerThumbnailUrl)
                .currentParticipants(currentParticipants)
                .build();
    }

}
