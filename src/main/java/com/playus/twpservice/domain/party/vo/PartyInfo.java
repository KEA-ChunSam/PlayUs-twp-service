package com.playus.twpservice.domain.party.vo;

import com.playus.twpservice.domain.party.dto.partybymatch.PartyInfoResponse;
import com.playus.twpservice.domain.party.enums.PartyAgeGroup;
import com.playus.twpservice.domain.party.enums.PartyGender;
import com.playus.twpservice.domain.party.enums.PartyJoinMethod;
import com.playus.twpservice.domain.party.feign.response.PartyWriterInfoFeignResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PartyInfo {
    private Long partyId;
    private String title;
    private String text;
    private Long writerId;
    private Long matchId;
    private List<Long> userIdList;
    private PartyJoinMethod partyJoinMethod;
    private PartyGender partyGender;
    private List<Integer> ages;
    private Long currentParticipantsCount;
    private Long maximumParticipants;
    private List<String> thumbnailUrls;

    private String writerName;
    private String writerGender;
    private String writerThumbnailUrl;

    private List<String> userThumbnailUrls;
    private LocalDateTime matchDate;

    public void updateUserThumbnailUrls(List<String> userThumbnailUrls) {
        this.userThumbnailUrls = userThumbnailUrls;
    }

    public void updateWriterInfo(PartyWriterInfoFeignResponse partyWriterInfoFeignResponse) {
         this.writerName = partyWriterInfoFeignResponse.writerName();
         this.writerGender = partyWriterInfoFeignResponse.writerGender();
         this.userThumbnailUrls.add(0, partyWriterInfoFeignResponse.writerThumbnailUrl());
    }

    public void updateMatchDate(LocalDateTime matchDate) {
        this.matchDate = matchDate;
    }

    public PartyInfoResponse toResponse() {
        return PartyInfoResponse.of(
                partyId,
                title,
                partyJoinMethod,
                ages.stream().map(PartyAgeGroup::getAgeGroupByAge).toList(),
                partyGender,
                writerName,
                writerGender,
                matchDate,
                currentParticipantsCount + 1, // 방장 추가
                maximumParticipants,
                thumbnailUrls,
                userThumbnailUrls
        );
    }
}
