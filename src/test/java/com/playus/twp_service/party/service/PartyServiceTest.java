package com.playus.twp_service.party.service;

import com.playus.twp_service.IntegrationTestSupport;
import com.playus.twp_service.domain.party.dto.party_create.PartyCreateRequest;
import com.playus.twp_service.domain.party.dto.party_create.PartyCreateResponse;
import com.playus.twp_service.domain.party.entity.Party;
import com.playus.twp_service.domain.party.entity.PartyAge;
import com.playus.twp_service.domain.party.entity.PartyJoin;
import com.playus.twp_service.domain.party.enums.PartyGender;
import com.playus.twp_service.domain.party.enums.PartyJoinMethod;
import com.playus.twp_service.domain.party.enums.Status;
import com.playus.twp_service.domain.party.repository.write.PartyAgeRepository;
import com.playus.twp_service.domain.party.repository.write.PartyJoinRepository;
import com.playus.twp_service.domain.party.repository.write.PartyRepository;
import com.playus.twp_service.domain.party.service.PartyService;
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

    @AfterEach
    void tearDown() {
        partyAgeRepository.deleteAllInBatch();
        partyJoinRepository.deleteAllInBatch();
        partyRepository.deleteAllInBatch();
    }

    @DisplayName("직관팟을 생성할 수 있다.")
    @Test
    void createParty() {
        // given
        Long userId = 1L;
        PartyCreateRequest request = PartyCreateRequest.of("title", "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, "url", "message");

        // when
        PartyCreateResponse result = partyService.createParty(userId, request);

        // then
        assertThat(partyRepository.count()).isEqualTo(1);
        assertThat(partyJoinRepository.count()).isEqualTo(1);

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

        List<PartyAge> savedPartyAges = partyAgeRepository.findAll();
        assertThat(savedPartyAges).hasSize(2);
        assertThat(savedPartyAges)
                .extracting("age")
                .containsExactlyInAnyOrder(10, 20);
        assertThat(result).extracting("success").isEqualTo(true);
    }
}
