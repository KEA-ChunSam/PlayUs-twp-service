package com.playus.twpservice.domain.party.vo;

import com.playus.twpservice.domain.party.dto.appliedparty.AppliedPartyResponse;
import com.playus.twpservice.domain.party.dto.info.PartyInfoResponse;
import com.playus.twpservice.domain.party.dto.detail.PartyDetailResponse;
import com.playus.twpservice.domain.party.enums.PartyAgeGroup;
import com.playus.twpservice.domain.party.enums.PartyGender;
import com.playus.twpservice.domain.party.enums.PartyJoinMethod;
import com.playus.twpservice.domain.party.enums.PartyJoinRequestStatus;
import com.playus.twpservice.domain.common.feign.response.PartyWriterInfoFeignResponse;
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
    private PartyJoinRequestStatus partyJoinRequestStatus;
    private List<Integer> ages;
    private Long currentParticipantsCount;
    private Long maximumParticipants;
    private Long chatRoomId;
    private List<String> thumbnailUrls;

    private String writerName;
    private String writerGender;
    private String writerThumbnailUrl;
    private int writerAge;

    private List<String> userThumbnailUrls;
    private LocalDateTime matchDate;

    public void updateUserThumbnailUrls(List<String> userThumbnailUrls) {
        this.userThumbnailUrls = userThumbnailUrls;
    }

    public void updateWriterInfo(PartyWriterInfoFeignResponse partyWriterInfoFeignResponse) {
        this.writerName = partyWriterInfoFeignResponse.writerName();
        this.writerGender = partyWriterInfoFeignResponse.writerGender();
        this.writerAge = partyWriterInfoFeignResponse.writerAge();
        this.userThumbnailUrls.add(0, partyWriterInfoFeignResponse.writerThumbnailUrl());
    }

    public void updateWriterInfoWhenUpdatingWriterThumbnailOnly(PartyWriterInfoFeignResponse partyWriterInfoFeignResponse) {
        this.writerName = partyWriterInfoFeignResponse.writerName();
        this.writerGender = partyWriterInfoFeignResponse.writerGender();
        this.writerAge = partyWriterInfoFeignResponse.writerAge();
        this.writerThumbnailUrl = partyWriterInfoFeignResponse.writerThumbnailUrl();
    }

    public void updateMatchDate(LocalDateTime matchDate) {
        this.matchDate = matchDate;
    }

    public PartyInfoResponse toResponse() {
        return PartyInfoResponse.of(
                partyId,
                writerId,
                title,
                partyJoinMethod,
                ages.stream().map(PartyAgeGroup::getAgeGroupByAge).toList(),
                partyGender,
                writerName,
                writerGender,
                writerAge,
                matchDate,
                currentParticipantsCount,
                maximumParticipants,
                thumbnailUrls,
                userThumbnailUrls
        );
    }

    public PartyDetailResponse toPartyDetailResponse() {
        return PartyDetailResponse.of(
                partyId,
                writerId,
                title,
                partyJoinMethod,
                text,
                ages.stream().map(PartyAgeGroup::getAgeGroupByAge).toList(),
                partyGender,
                writerName,
                writerGender,
                writerAge,
                matchDate,
                currentParticipantsCount,
                maximumParticipants,
                chatRoomId,
                thumbnailUrls,
                userThumbnailUrls
        );
    }

    // 타 서비스 없이 테스트할 시 writer 관련 주석화하기
    public AppliedPartyResponse toAppliedPartyResponse() {
        return AppliedPartyResponse.of(
                partyId,
                title,
                ages.stream().map(PartyAgeGroup::getAgeDescriptionByAge).toList(),
                partyGender.getDescription(),
                partyJoinRequestStatus.getMessage(),
                writerId,
                writerName,
                writerGender,
                PartyAgeGroup.getAgeDescriptionByAge(writerAge),
                writerThumbnailUrl,
                currentParticipantsCount
        );
    }
}
