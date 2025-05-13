package com.playus.twpservice.domain.party.service;

import com.playus.twpservice.IntegrationTestSupport;
import com.playus.twpservice.domain.party.document.PartyAgeDocument;
import com.playus.twpservice.domain.party.document.PartyDocument;
import com.playus.twpservice.domain.party.document.PartyJoinDocument;
import com.playus.twpservice.domain.party.document.PartyThumbnailUrlDocument;
import com.playus.twpservice.domain.party.dto.partybymatch.PartiesByMatchResponse;
import com.playus.twpservice.domain.party.enums.PartyGender;
import com.playus.twpservice.domain.party.enums.PartyJoinMethod;
import com.playus.twpservice.domain.party.enums.PartyJoinRequestStatus;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
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


    @AfterEach
    void tearDown() {
        partyReadOnlyRepository.deleteAll();
        partyAgeReadOnlyRepository.deleteAll();
        partyThumbnailUrlReadOnlyRepository.deleteAll();
        partyJoinReadOnlyRepository.deleteAll();
    }

    @DisplayName("특정 경기에 대한 직관팟을 불러올 수 있다.")
    @Test
    void getPartiesBy() {
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
        List<PartiesByMatchResponse> result = partyReadOnlyService.getPartiesBy(matchId);

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
    void getPartiesBy_EMPTY_PARTY() {

        // given
        Long matchId = 1L;

        // when
        List<PartiesByMatchResponse> result = partyReadOnlyService.getPartiesBy(matchId);

        // then
        assertThat(result).isEmpty();
    }
}
