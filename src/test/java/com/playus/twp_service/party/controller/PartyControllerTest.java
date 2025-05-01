package com.playus.twp_service.party.controller;

import com.playus.twp_service.ControllerTestSupport;
import com.playus.twp_service.party.dto.party_create.PartyCreateRequest;
import com.playus.twp_service.party.dto.party_create.PartyCreateResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

class PartyControllerTest extends ControllerTestSupport {




    @DisplayName("직관팟을 생성할 수 있다.")
    @Test
    void createParty() {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("title", "선착순", "남자만", List.of("10대", "20대"), "message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(PartyCreateRequest.class))).willReturn(Mono.just(mockResponse));

        // when // then
        webTestClient.post()
                .uri("/party")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.partyId").isEqualTo(1L)
                .jsonPath("$.success").isEqualTo("true");
    }
}
