package com.playus.twp_service.party.repository.read;

import com.playus.twp_service.IntegrationTestSupport;
import com.playus.twp_service.party.document.PartyDocument;
import com.playus.twp_service.party.enums.Method;
import com.playus.twp_service.party.enums.PartyGender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PartyReadOnlyRepositoryTest extends IntegrationTestSupport {

    @Autowired
    PartyReadOnlyRepository partyReadOnlyRepository;

    @AfterEach
    void tearDown() {
        partyReadOnlyRepository.deleteAll();
    }

    @DisplayName("Party를 MongoDB에서 Long 타입 id 통해 가져올 수 있다.")
    @Test
    void savePartyReadOnlyRepo() {
        // given
        Long firstId = 1L;
        Long secondId = 2L;

        PartyDocument p1 = PartyDocument.createForOnlyTest(firstId, "p1", "text1", 1L, 5L, "url1", PartyGender.MALE, Method.FIRST_COME);
        PartyDocument p2 = PartyDocument.createForOnlyTest(secondId, "p2", "text", 1L, 5L, "url", PartyGender.MALE, Method.FIRST_COME);
        partyReadOnlyRepository.saveAll(List.of(p1, p2));

        // when
        Optional<PartyDocument> result = partyReadOnlyRepository.findById(firstId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).extracting("id", "title", "text", "thumbnailUrl")
                .containsExactly(firstId, "p1", "text1", "url1");
    }
}
