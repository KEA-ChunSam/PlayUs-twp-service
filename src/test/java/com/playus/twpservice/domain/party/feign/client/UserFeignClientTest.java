package com.playus.twpservice.domain.party.feign.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.playus.twpservice.IntegrationTestSupport;
import com.playus.twpservice.domain.party.feign.response.PartyUserThumbnailUrlListResponse;
import com.playus.twpservice.domain.party.feign.response.PartyWriterInfoFeignResponse;
import com.playus.twpservice.global.response.ErrorResponse;
import feign.FeignException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.assertj.core.api.Assertions.*;

class UserFeignClientTest extends IntegrationTestSupport {

    @Autowired
    UserFeignClient userFeignClient;

    @DisplayName("사용자의 썸네일 URL을 가져올 수 있다.")
    @Test
    void getPartyUserThumbnailUrls() throws JsonProcessingException {
        // given
        List<Long> userIdList = List.of(1L, 2L);
        PartyUserThumbnailUrlListResponse expectedResponse = PartyUserThumbnailUrlListResponse.of(List.of("http://user-thumb", "http://user2-thumb"));
        stubFor(post(urlEqualTo("/user/api/thumbnails"))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(userIdList)))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(expectedResponse))
                ));

        // when
        PartyUserThumbnailUrlListResponse result = userFeignClient.getPartyUserThumbnailUrls(userIdList);

        // then
        assertThat(result.thumbnailUrls())
                .hasSize(2)
                .containsExactly("http://user-thumb", "http://user2-thumb");
    }

    @DisplayName("사용자가 존재하지 않을 수 있다.")
    @Test
    void getPartyUserThumbnailurls_INVALID_PK() throws JsonProcessingException {
        // given
        List<Long> userIdList = List.of(1L, 2L);
        ErrorResponse errorResponse = ErrorResponse.notFoundError("사용자가 존재하지 않습니다!");
        stubFor(post(urlEqualTo("/user/api/thumbnails"))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(userIdList)))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.NOT_FOUND.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(errorResponse))
                ));

        // when // then
        assertThatThrownBy(() -> userFeignClient.getPartyUserThumbnailUrls(userIdList))
                .isInstanceOf(FeignException.class)
                .hasMessageContaining("사용자가 존재하지 않습니다!");
    }

    @DisplayName("사용자의 썸네일 URL이 비어 있을 수 있다.")
    @Test
    void getPartyUserThumbnailUrls_EMPTY_THUMB() throws JsonProcessingException {
        // given
        List<Long> userIdList = List.of(1L, 2L);
        PartyUserThumbnailUrlListResponse expectedResponse = PartyUserThumbnailUrlListResponse.of(List.of());
        stubFor(post(urlEqualTo("/user/api/thumbnails"))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(userIdList)))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(expectedResponse))
                ));

        // when
        PartyUserThumbnailUrlListResponse result = userFeignClient.getPartyUserThumbnailUrls(userIdList);

        // then
        assertThat(result.thumbnailUrls()).isEmpty();
    }

    @DisplayName("직관팟 작성자의 정보를 가져올 수 있다.")
    @Test
    void getWriterInfo() throws JsonProcessingException {
        // given
        List<Long> writerIdList = List.of(1L, 2L);
        List<PartyWriterInfoFeignResponse> expectedResponse = List.of(
                PartyWriterInfoFeignResponse.of(1L, "user1", "남성", "http://user1-thumb"),
                PartyWriterInfoFeignResponse.of(2L, "user2", "여성", "http://user2-thumb")
        );

        stubFor(post(urlEqualTo("/user/api/writers"))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(writerIdList)))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(expectedResponse))
                ));

        // when
        List<PartyWriterInfoFeignResponse> result = userFeignClient.getWriterInfo(writerIdList);

        // then
        assertThat(result).hasSize(2)
                .extracting("id", "writerName", "writerGender", "writerThumbnailUrl")
                .containsExactly(
                        tuple(1L, "user1", "남성", "http://user1-thumb"),
                        tuple(2L, "user2", "여성", "http://user2-thumb")
                );
    }

    @DisplayName("직관팟 작성자가 존재하지 않을 수 있다.")
    @Test
    void getWriterInfo_INVALID_PK() throws JsonProcessingException {
        // given
        List<Long> writerIdList = List.of(1L, 2L);
        ErrorResponse errorResponse = ErrorResponse.notFoundError("사용자가 존재하지 않습니다!");
        stubFor(post(urlEqualTo("/user/api/writers"))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(writerIdList)))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.NOT_FOUND.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(errorResponse))
                ));

        // when // then
        assertThatThrownBy(() -> userFeignClient.getWriterInfo(writerIdList))
                .isInstanceOf(FeignException.class)
                .hasMessageContaining("사용자가 존재하지 않습니다!");
    }

}
