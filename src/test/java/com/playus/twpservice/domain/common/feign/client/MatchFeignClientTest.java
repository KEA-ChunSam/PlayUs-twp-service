package com.playus.twpservice.domain.common.feign.client;

import com.playus.twpservice.IntegrationTestSupport;
import com.playus.twpservice.global.response.ErrorResponse;
import feign.FeignException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MatchFeignClientTest extends IntegrationTestSupport {

    @Autowired
    MatchFeignClient matchFeignClient;

    @DisplayName("경기 날짜를 가져올 수 있다.")
    @Test
    void getMatchDate() throws Exception {
        // given
        Long matchId = 1L;
        LocalDateTime expectedResponse = LocalDateTime.of(2023, 10, 1, 12, 0);

        stubFor(get(urlEqualTo("/match/api/date?matchId=" + matchId))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(expectedResponse))
                ));

        // when
        LocalDateTime matchDate = matchFeignClient.getMatchDate(matchId);

        // then
        assertThat(matchDate)
                .isEqualTo(expectedResponse);
    }

    @DisplayName("경기 날짜를 가져오지 못할 수 있다.")
    @Test
    void getMatchDate_404() throws Exception {
        // given
        Long matchId = 1L;
        ErrorResponse errorResponse = ErrorResponse.notFoundError("찾을 수 없습니다!");

        stubFor(get(urlEqualTo("/match/api/date?matchId=" + matchId))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.NOT_FOUND.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(errorResponse))
                ));

        // when // then
        assertThatThrownBy(() -> matchFeignClient.getMatchDate(matchId))
                .isInstanceOf(FeignException.class)
                .hasMessageContaining("찾을 수 없습니다!");
    }

}
