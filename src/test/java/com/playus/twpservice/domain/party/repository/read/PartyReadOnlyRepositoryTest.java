package com.playus.twpservice.domain.party.repository.read;

import com.playus.twpservice.IntegrationTestSupport;
import com.playus.twpservice.domain.party.document.PartyAgeDocument;
import com.playus.twpservice.domain.party.document.PartyDocument;
import com.playus.twpservice.domain.party.document.PartyJoinDocument;
import com.playus.twpservice.domain.party.document.PartyThumbnailUrlDocument;
import com.playus.twpservice.domain.party.enums.PartyGender;
import com.playus.twpservice.domain.party.enums.PartyJoinMethod;
import com.playus.twpservice.domain.party.enums.PartyJoinRequestStatus;
import com.playus.twpservice.domain.party.vo.PartyInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class PartyReadOnlyRepositoryTest extends IntegrationTestSupport {

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

    @DisplayName("특정 경기에 대한 직관팟 정보를 가져올 수 있다.")
    @Test
    void findPartyInfoListByMatchId() {
        // given
        Long matchId = 1L;

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
        List<PartyInfo> result = partyReadOnlyRepository.findPartyInfoBy(matchId);

        // then
        assertThat(result).hasSize(2)
                .extracting("partyId", "title", "writerId", "userIdList", "partyJoinMethod", "partyGender", "ages",
                        "currentParticipantsCount", "maximumParticipants", "thumbnailUrls")
                .containsExactlyInAnyOrder(
                        tuple(1L, "title1", 1L, List.of(4L, 5L), PartyJoinMethod.FIRST_COME, PartyGender.MALE, List.of(10), 2L, 10L, List.of("thumbnailUrl1", "thumbnailUrl2")),
                        tuple(2L, "title2", 2L, List.of(), PartyJoinMethod.RESERVATION, PartyGender.FEMALE, List.of(20), 0L, 10L, List.of())
                );
    }

    @DisplayName("특정 경기에 대한 직관팟이 없을 수 있다.")
    @Test
    void findPartyInfoListByMatchId_EMPTY_PARTY() {
        // given
        Long matchId = 1L;

        // when
        List<PartyInfo> result = partyReadOnlyRepository.findPartyInfoBy(matchId);

        // then
        assertThat(result).isEmpty();
    }

    @DisplayName("직관팟의 자세한 정보를 가져올 수 있다.")
    @Test
    void findPartyDetailBy() {
        Long partyId = 1L;
        Long matchId = 1L;

        PartyDocument p1 = PartyDocument.createForOnlyTest(partyId, "title1", "text1", 1L, 10L,
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
        Optional<PartyInfo> result = partyReadOnlyRepository.findPartyDetailBy(partyId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get())
                .extracting("partyId", "title", "text", "writerId", "userIdList", "partyJoinMethod", "partyGender", "ages",
                        "currentParticipantsCount", "maximumParticipants", "thumbnailUrls")
                .containsExactly(
                        1L, "title1", "text1", 1L, List.of(4L, 5L), PartyJoinMethod.FIRST_COME, PartyGender.MALE,
                        List.of(10), 2L, 10L, List.of("thumbnailUrl1", "thumbnailUrl2")
                );

    }

    @DisplayName("ID에 대한 직관팟이 존재하지 않을 수 있다.")
    @Test
    void findPartyDetailBy_NOT_FOUND() {

        // given
        Long notFoundPartyId = 5L;

        // when
        Optional<PartyInfo> result = partyReadOnlyRepository.findPartyDetailBy(notFoundPartyId);

        // then
        assertThat(result).isEmpty();
    }
}
