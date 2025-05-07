package com.playus.twpservice.domain.party.service;

import com.playus.twpservice.IntegrationTestSupport;
import com.playus.twpservice.domain.party.dto.party_create.PartyCreateRequest;
import com.playus.twpservice.domain.party.dto.party_create.PartyCreateResponse;
import com.playus.twpservice.domain.party.entity.Party;
import com.playus.twpservice.domain.party.entity.PartyAge;
import com.playus.twpservice.domain.party.entity.PartyJoin;
import com.playus.twpservice.domain.party.entity.PartyThumbnailUrl;
import com.playus.twpservice.domain.party.enums.PartyGender;
import com.playus.twpservice.domain.party.enums.PartyJoinMethod;
import com.playus.twpservice.domain.party.enums.Status;
import com.playus.twpservice.domain.party.repository.write.PartyAgeRepository;
import com.playus.twpservice.domain.party.repository.write.PartyJoinRepository;
import com.playus.twpservice.domain.party.repository.write.PartyRepository;
import com.playus.twpservice.domain.party.repository.write.PartyThumbnailUrlRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PartyServiceTest extends IntegrationTestSupport {

    @Autowired
    private PartyService partyService;

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private PartyJoinRepository partyJoinRepository;

    @Autowired
    private PartyAgeRepository partyAgeRepository;

    @Autowired
    private PartyThumbnailUrlRepository partyThumbnailUrlRepository;

    @AfterEach
    void tearDown() {
        partyThumbnailUrlRepository.deleteAllInBatch();
        partyAgeRepository.deleteAllInBatch();
        partyJoinRepository.deleteAllInBatch();
        partyRepository.deleteAllInBatch();
    }

    @DisplayName("직관팟을 생성할 수 있다.")
    @Test
    void createParty() {
        // given
        Long userId = 1L;
        PartyCreateRequest request = PartyCreateRequest.of("title", "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, List.of("url"), "message");

        // when
        PartyCreateResponse result = partyService.createParty(userId, request);

        // then
        assertThat(partyRepository.count()).isEqualTo(1);
        assertThat(partyJoinRepository.count()).isEqualTo(1);
        assertThat(partyThumbnailUrlRepository.count()).isEqualTo(1);

        Party savedParty = partyRepository.findAll().get(0);
        assertThat(savedParty.getTitle()).isEqualTo("title");
        assertThat(savedParty.getMinimumParticipants()).isEqualTo(1L);
        assertThat(savedParty.getMaximumParticipants()).isEqualTo(10L);
        assertThat(savedParty.getPartyGender()).isEqualTo(PartyGender.MALE);
        assertThat(savedParty.getPartyJoinMethod()).isEqualTo(PartyJoinMethod.FIRST_COME);

        PartyJoin savedPartyJoin = partyJoinRepository.findAll().get(0);
        assertThat(savedPartyJoin.getUserId()).isEqualTo(userId);
        assertThat(savedPartyJoin.getStatus()).isEqualTo(Status.ACCEPT);
        assertThat(savedPartyJoin.getRequireMessage()).isNull();

        PartyThumbnailUrl savedPartyUrl = partyThumbnailUrlRepository.findAll().get(0);
        assertThat(savedPartyUrl.getThumbnailUrl()).isEqualTo("url");

        List<PartyAge> savedPartyAges = partyAgeRepository.findAll();
        assertThat(savedPartyAges).hasSize(2);
        assertThat(savedPartyAges)
                .extracting("age")
                .containsExactlyInAnyOrder(10, 20);
        assertThat(result).extracting("success").isEqualTo(true);

        Long savedPartyId = savedParty.getId();
        assertThat(savedPartyJoin.getParty().getId()).isEqualTo(savedPartyId);
        assertThat(savedPartyUrl.getParty().getId()).isEqualTo(savedPartyId);
        assertThat(savedPartyAges).allMatch(age -> age.getParty().getId().equals(savedPartyId));
    }

    @DisplayName("썸네일 URL이 비어 있을 때에도 직관팟을 생성할 수 있다.")
    @Test
    void createParty_EMPTY_IMAGE_URL() {
        // given
        Long userId = 1L;
        PartyCreateRequest request = PartyCreateRequest.of("title", "선착순", "남자만",
                List.of("10대", "20대"), 1L, 10L, List.of(), "message");

        // when
        partyService.createParty(userId, request);

        // then
        assertThat(partyRepository.count()).isEqualTo(1);
        assertThat(partyJoinRepository.count()).isEqualTo(1);
        assertThat(partyAgeRepository.count()).isEqualTo(2);
        assertThat(partyThumbnailUrlRepository.count()).isZero();
    }

    @DisplayName("썸네일 URL이 NULL일 때에도 직관팟을 생성할 수 있다.")
    @Test
    void createParty_NULL_IMAGE_URL() {
        // given
        Long userId = 1L;
        PartyCreateRequest request = PartyCreateRequest.of("title", "선착순", "남자만",
                List.of("10대", "20대"), 1L, 10L, null, "message");

        // when
        partyService.createParty(userId, request);

        // then
        assertThat(partyRepository.count()).isEqualTo(1);
        assertThat(partyJoinRepository.count()).isEqualTo(1);
        assertThat(partyAgeRepository.count()).isEqualTo(2);
        assertThat(partyThumbnailUrlRepository.count()).isZero();
    }


}
