package com.playus.twpservice.domain.party.service;

import com.playus.twpservice.IntegrationTestSupport;
import com.playus.twpservice.domain.party.document.PartyAgeDocument;
import com.playus.twpservice.domain.party.document.PartyDocument;
import com.playus.twpservice.domain.party.document.PartyJoinDocument;
import com.playus.twpservice.domain.party.document.PartyThumbnailUrlDocument;
import com.playus.twpservice.domain.party.dto.partybymatch.PartyInfoResponse;
import com.playus.twpservice.domain.party.dto.partydescription.PartyDetailResponse;
import com.playus.twpservice.domain.party.enums.PartyGender;
import com.playus.twpservice.domain.party.enums.PartyJoinMethod;
import com.playus.twpservice.domain.party.enums.PartyJoinRequestStatus;
import com.playus.twpservice.domain.party.exception.document.PartyDocumentException;
import com.playus.twpservice.domain.party.feign.client.MatchFeignClient;
import com.playus.twpservice.domain.party.feign.client.UserFeignClient;
import com.playus.twpservice.domain.party.feign.response.PartyUserThumbnailUrlListResponse;
import com.playus.twpservice.domain.party.feign.response.PartyWriterInfoFeignResponse;
import com.playus.twpservice.domain.party.repository.read.PartyAgeReadOnlyRepository;
import com.playus.twpservice.domain.party.repository.read.PartyJoinReadOnlyRepository;
import com.playus.twpservice.domain.party.repository.read.PartyReadOnlyRepository;
import com.playus.twpservice.domain.party.repository.read.PartyThumbnailUrlReadOnlyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class PartyReadOnlyServiceTest extends IntegrationTestSupport {

    @Autowired
    PartyReadOnlyService partyReadOnlyService;

    @Autowired
    PartyReadOnlyRepository partyReadOnlyRepository;

    @Autowired
    PartyAgeReadOnlyRepository partyAgeReadOnlyRepository;

    @Autowired
    PartyThumbnailUrlReadOnlyRepository partyThumbnailUrlReadOnlyRepository;

    @Autowired
    PartyJoinReadOnlyRepository partyJoinReadOnlyRepository;

    @MockitoBean
    protected UserFeignClient userFeignClient;

    @MockitoBean
    protected MatchFeignClient matchFeignClient;


    @AfterEach
    void tearDown() {
        partyReadOnlyRepository.deleteAll();
        partyAgeReadOnlyRepository.deleteAll();
        partyThumbnailUrlReadOnlyRepository.deleteAll();
        partyJoinReadOnlyRepository.deleteAll();
    }

    @DisplayName("특정 경기에 대한 직관팟을 불러올 수 있다.")
    @Test
    void getPartyInfoListByMatchId() {
        // given
        Long matchId = 1L;

        given(userFeignClient.getPartyUserThumbnailUrls(List.of())).willReturn(PartyUserThumbnailUrlListResponse.of(new ArrayList<>()));
        given(userFeignClient.getPartyUserThumbnailUrls(List.of(4L, 5L))).willReturn(
                PartyUserThumbnailUrlListResponse.of(new ArrayList<>(List.of("http://user1", "http://user2")))
        );

        given(userFeignClient.getWriterInfo(List.of(1L, 2L))).willReturn(List.of(
                PartyWriterInfoFeignResponse.of(1L, "writer1", "남성", "http://writer1-thumbnail"),
                PartyWriterInfoFeignResponse.of(2L, "writer2", "여성", "http://writer2-thumbnail")
        ));

        LocalDateTime matchDate = LocalDateTime.of(2025, 3, 22, 14, 0);
        given(matchFeignClient.getMatchDate(matchId)).willReturn(matchDate);

        PartyDocument p1 = PartyDocument.createForOnlyTest(1L, "title1", "text1", 1L, 10L,
                "http://thumbnail", PartyGender.MALE, PartyJoinMethod.FIRST_COME, 1L, matchId, "chatRoomId");
        PartyDocument p2 = PartyDocument.createForOnlyTest(2L, "title2", "text2", 1L, 10L,
                "http://thumbnail", PartyGender.FEMALE, PartyJoinMethod.RESERVATION, 2L, matchId, "chatRoom2Id");
        PartyDocument p3 = PartyDocument.createForOnlyTest(3L, "title3", "text3", 1L, 10L,
                "http://thumbnail", PartyGender.NO_MATTER, PartyJoinMethod.RESERVATION, 3L, matchId + 1, "chatRoom3Id");

        List<PartyDocument> partyDocuments = partyReadOnlyRepository.saveAll(List.of(p1, p2, p3));

        PartyAgeDocument pa1 = PartyAgeDocument.createForOnlyTest(1L, partyDocuments.get(0), 10);
        PartyAgeDocument pa2 = PartyAgeDocument.createForOnlyTest(2L, partyDocuments.get(1), 20);
        partyAgeReadOnlyRepository.saveAll(List.of(pa1, pa2));

        PartyThumbnailUrlDocument ptu1 = PartyThumbnailUrlDocument.createForOnlyTest(1L, partyDocuments.get(0), "thumbnailUrl1");
        PartyThumbnailUrlDocument ptu2 = PartyThumbnailUrlDocument.createForOnlyTest(2L, partyDocuments.get(0), "thumbnailUrl2");
        partyThumbnailUrlReadOnlyRepository.saveAll(List.of(ptu1, ptu2));

        PartyJoinDocument pj1 = PartyJoinDocument.createForOnlyTest(1L, 4L, partyDocuments.get(0), PartyJoinRequestStatus.ACCEPT, null);
        PartyJoinDocument pj2 = PartyJoinDocument.createForOnlyTest(2L, 5L, partyDocuments.get(0), PartyJoinRequestStatus.WAIT, "가입 원합니다!");
        partyJoinReadOnlyRepository.saveAll(List.of(pj1, pj2));

        // when
        List<PartyInfoResponse> result = partyReadOnlyService.getPartyInfoListByMatchId(matchId);

        // then
        assertThat(result).hasSize(2)

                .extracting("partyId", "title", "partyJoinMethod", "partyAges", "availableGender", "authorName", "authorGender",
                        "matchDate", "currentParticipantsCount", "maximumParticipantsCount", "partyThumbnailUrls", "userThumbnailUrls")

                .containsExactlyInAnyOrder(
                        tuple(1L, "title1", PartyJoinMethod.FIRST_COME.getDescription(), List.of("10대"), PartyGender.MALE.getDescription(), "writer1", "남성",
                                matchDate, 3L, 10L, List.of("thumbnailUrl1", "thumbnailUrl2"), List.of("http://writer1-thumbnail", "http://user1", "http://user2")),

                        tuple(2L, "title2", PartyJoinMethod.RESERVATION.getDescription(), List.of("20대"), PartyGender.FEMALE.getDescription(), "writer2", "여성",
                                matchDate, 1L, 10L, List.of(), List.of("http://writer2-thumbnail"))
                );
    }

    @DisplayName("특정 경기에 대한 직관팟이 없을 수 있다.")
    @Test
    void getPartyInfoList_EMPTY_PARTYByMatchId() {

        // given
        Long matchId = 1L;

        // when
        List<PartyInfoResponse> result = partyReadOnlyService.getPartyInfoListByMatchId(matchId);

        // then
        assertThat(result).isEmpty();
    }

    @DisplayName("직관팟의 자세한 정보를 가져올 수 있다.")
    @Test
    void getPartyDetail() {

        Long partyId = 1L;
        Long writerId = 1L;
        Long matchId = 1L;

        given(userFeignClient.getPartyUserThumbnailUrls(List.of())).willReturn(PartyUserThumbnailUrlListResponse.of(new ArrayList<>()));
        given(userFeignClient.getPartyUserThumbnailUrls(List.of(4L, 5L))).willReturn(
                PartyUserThumbnailUrlListResponse.of(new ArrayList<>(List.of("http://user1", "http://user2")))
        );

        given(userFeignClient.getWriterInfo(List.of(writerId))).willReturn(List.of(
                PartyWriterInfoFeignResponse.of(1L, "writer1", "남성", "http://writer1-thumbnail")
        ));

        LocalDateTime matchDate = LocalDateTime.of(2025, 3, 22, 14, 0);
        given(matchFeignClient.getMatchDate(matchId)).willReturn(matchDate);

        PartyDocument p1 = PartyDocument.createForOnlyTest(partyId, "title1", "text1", 1L, 10L,
                "http://thumbnail", PartyGender.MALE, PartyJoinMethod.FIRST_COME, writerId, matchId, "chatRoomId");
        PartyDocument p2 = PartyDocument.createForOnlyTest(2L, "title2", "text2", 1L, 10L,
                "http://thumbnail", PartyGender.FEMALE, PartyJoinMethod.RESERVATION, 2L, matchId, "chatRoom2Id");
        PartyDocument p3 = PartyDocument.createForOnlyTest(3L, "title3", "text3", 1L, 10L,
                "http://thumbnail", PartyGender.NO_MATTER, PartyJoinMethod.RESERVATION, 3L, matchId + 1, "chatRoom3Id");

        List<PartyDocument> partyDocuments = partyReadOnlyRepository.saveAll(List.of(p1, p2, p3));

        PartyAgeDocument pa1 = PartyAgeDocument.createForOnlyTest(1L, partyDocuments.get(0), 10);
        PartyAgeDocument pa2 = PartyAgeDocument.createForOnlyTest(2L, partyDocuments.get(1), 20);
        partyAgeReadOnlyRepository.saveAll(List.of(pa1, pa2));

        PartyThumbnailUrlDocument ptu1 = PartyThumbnailUrlDocument.createForOnlyTest(1L, partyDocuments.get(0), "thumbnailUrl1");
        PartyThumbnailUrlDocument ptu2 = PartyThumbnailUrlDocument.createForOnlyTest(2L, partyDocuments.get(0), "thumbnailUrl2");
        partyThumbnailUrlReadOnlyRepository.saveAll(List.of(ptu1, ptu2));

        PartyJoinDocument pj1 = PartyJoinDocument.createForOnlyTest(1L, 4L, partyDocuments.get(0), PartyJoinRequestStatus.ACCEPT, null);
        PartyJoinDocument pj2 = PartyJoinDocument.createForOnlyTest(2L, 5L, partyDocuments.get(0), PartyJoinRequestStatus.WAIT, "가입 원합니다!");
        partyJoinReadOnlyRepository.saveAll(List.of(pj1, pj2));

        // when
        PartyDetailResponse result = partyReadOnlyService.getPartyDetail(partyId);

        // then
        assertThat(result)

                .extracting("partyId", "title", "partyJoinMethod", "text", "partyAges", "availableGender", "authorName", "authorGender",
                        "matchDate", "currentParticipantsCount", "maximumParticipantsCount", "partyThumbnailUrls", "userThumbnailUrls")

                .containsExactly(
                        1L, "title1", PartyJoinMethod.FIRST_COME.getDescription(), "text1", List.of("10대"), PartyGender.MALE.getDescription(), "writer1", "남성",
                                matchDate, 3L, 10L, List.of("thumbnailUrl1", "thumbnailUrl2"), List.of("http://writer1-thumbnail", "http://user1", "http://user2")

                );
    }

    @DisplayName("직관팟 상세정보 조회 시, 직관팟이 존재하지 않을 수 있다.")
    @Test
    void getPartyDetail_NOT_FOUND() {

        Long partyId = 1L;

        // when // then
        assertThatThrownBy(() -> partyReadOnlyService.getPartyDetail(partyId))
                .isInstanceOf(PartyDocumentException.NotFoundException.class)
                .hasMessage("직관팟이 존재하지 않습니다!");
    }
}
