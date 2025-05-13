package com.playus.twpservice.domain.party.vo;

import com.playus.twpservice.domain.party.enums.PartyGender;
import com.playus.twpservice.domain.party.enums.PartyJoinMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PartySummary {
    private Long partyId;
    private String title;
    private Long writerId;
    private PartyJoinMethod partyJoinMethod;
    private PartyGender partyGender;
    private List<Integer> ages;
    private Long currentParticipantsCount;
    private Long maximumParticipants;
    private List<String> thumbnailUrls;
}
