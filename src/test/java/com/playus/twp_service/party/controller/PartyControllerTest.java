package com.playus.twp_service.party.controller;

import com.playus.twp_service.ControllerTestSupport;
import com.playus.twp_service.party.dto.party_create.PartyCreateRequest;
import com.playus.twp_service.party.dto.party_create.PartyCreateResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

class PartyControllerTest extends ControllerTestSupport {

    @DisplayName("직관팟을 생성할 수 있다.")
    @Test
    void createParty() {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("title", "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, "url", "message");
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



    @DisplayName("직관팟 생성 중 제목은 필수이다.")
    @NullAndEmptySource
    @ParameterizedTest(name = "title = {0}")
    void createParty_EMPTY_TITLE(String emptyTitle) {
        // given
        PartyCreateRequest request = PartyCreateRequest.of(emptyTitle, "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, "url","message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(PartyCreateRequest.class))).willReturn(Mono.just(mockResponse));

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "직관팟 제목이 비어 있습니다!");
    }




    @DisplayName("직관팟 생성 중 신청 방식은 필수이다.")
    @NullAndEmptySource
    @ParameterizedTest(name = "method = {0}")
    void createParty_EMPTY_METHOD(String emptyMethod) {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("제목", emptyMethod, "남자만", List.of("10대", "20대"), 1L, 10L, "url","message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(PartyCreateRequest.class))).willReturn(Mono.just(mockResponse));

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "신청 방식이 비어 있습니다!");
    }

    @DisplayName("직관팟 생성 중 신청 방식은 정해진 양식대로 입력해야 한다.")
    @CsvSource(value = {"FIRST_COME", "RESERVATION", "ABCDEFG", "ㄱㄴㄷㄹㅁㅂㅅㅇ", "!@#$%^&", "123456789"})
    @ParameterizedTest(name = "method = {0}")
    void createParty_INVALID_METHOD(String invalidMethod) {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("제목", invalidMethod, "남자만", List.of("10대", "20대"), 1L, 10L, "url","message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(PartyCreateRequest.class))).willReturn(Mono.just(mockResponse));

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "잘못된 신청 방식입니다!");
    }




    @DisplayName("직관팟 생성 중 참여 원하는 성별은 필수이다.")
    @NullAndEmptySource
    @ParameterizedTest(name = "gender = {0}")
    void createParty_EMPTY_GENDER(String emptyGender) {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", emptyGender, List.of("10대", "20대"), 1L, 10L, "url","message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(PartyCreateRequest.class))).willReturn(Mono.just(mockResponse));

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "참여 원하는 성별이 비어 있습니다!");
    }

    @DisplayName("직관팟 생성 중 성별은 정해진 양식대로 입력해야 한다.")
    @CsvSource(value = {"MALE", "FEMALE", "NO_MATTER", "ㄱㄴㄷㄹㅁㅂㅅㅇ", "!@#$%^&", "123456789"})
    @ParameterizedTest(name = "method = {0}")
    void createParty_INVALID_GENDER(String invalidGender) {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", invalidGender, List.of("10대", "20대"), 1L, 10L, "url","message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(PartyCreateRequest.class))).willReturn(Mono.just(mockResponse));

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "잘못된 성별 형식입니다!");
    }




    @DisplayName("직관팟 생성 중 참여자 나이는 필수이다.")
    @NullAndEmptySource
    @ParameterizedTest(name = "age = {0}")
    void createParty_EMPTY_AGE(List<String> emptyAgeList) {

        // given
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", emptyAgeList, 1L, 10L, "url","message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(PartyCreateRequest.class))).willReturn(Mono.just(mockResponse));

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "참여자 나이가 비어 있습니다!");
    }

    @DisplayName("직관팟 생성 중 참여자 나이는 정해진 양식대로 입력해야 한다.")
    @MethodSource("invalidAgeGroupProvider")
    @ParameterizedTest(name = "age = {0}")
    void createParty_INVALID_AGE(List<String> emptyAgeList) {

        // given
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", emptyAgeList, 1L, 10L, "url","message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(PartyCreateRequest.class))).willReturn(Mono.just(mockResponse));

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "잘못된 참여자 나이입니다!");
    }

    static Stream<Arguments> invalidAgeGroupProvider() {
        return Stream.of(
                Arguments.of(Arrays.asList("AGE_10", "AGE_20")),
                Arguments.of(Arrays.asList("10대", "AGE_10")),
                Arguments.of(Arrays.asList("10대", "20대", "AGE_30")),
                Arguments.of(Arrays.asList("ABCD", "DEFG", "HIJK")),
                Arguments.of(Arrays.asList("1234", "567", "89ABCD")),
                Arguments.of(Arrays.asList("!@#$", "%^&*", "*(*("))
        );
    }

    @DisplayName("직관팟 생성 중 참여자 나이는 정해진 수를 넘을 수 없다.")
    @Test
    void createParty_TOO_MANY_AGE() {

        // given
        List<String> tooManyAgeList = List.of("10대", "20대", "30대", "40대", "50대", "60대", "70대", "80대", "90대");
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", tooManyAgeList, 1L, 10L, "url","message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(PartyCreateRequest.class))).willReturn(Mono.just(mockResponse));

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "참여자 나이는 최대 6개까지 가능합니다!");
    }



    @DisplayName("직관팟 최소 인원은 필수이다.")
    @Test
    void createParty_EMPTY_MINIMUM() {
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", List.of("10대", "20대"), null, 10L, "url","message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(PartyCreateRequest.class))).willReturn(Mono.just(mockResponse));

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "최소 참여 인원이 비어 있습니다!");
    }

    @DisplayName("직관팟 최소 인원은 1 이상이여야 한다.")
    @Test
    void createParty_INVALID_MINIMUM() {
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", List.of("10대", "20대"), 0L, 10L, "url","message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(PartyCreateRequest.class))).willReturn(Mono.just(mockResponse));

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "최소 참여 인원은 1명 이상이여야 합니다!");
    }



    @DisplayName("직관팟 최대 인원은 필수이다.")
    @Test
    void createParty_EMPTY_MAXIMUM() {
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", List.of("10대", "20대"), 1L, null, "url","message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(PartyCreateRequest.class))).willReturn(Mono.just(mockResponse));

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "최대 참여 인원이 비어 있습니다!");
    }

    @DisplayName("직관팟 최소 인원은 최대 인원보다 커야 한다.")
    @Test
    void createParty_MINIMUM_MAXIMUM() {
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", List.of("10대", "20대"), 10L, 9L, "url","message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(PartyCreateRequest.class))).willReturn(Mono.just(mockResponse));

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "최소 참여 인원은 최대 참여 인원보다 클 수 없습니다!");
    }


    @DisplayName("직관팟 생성 중 사진 url은 필수이다.")
    @NullAndEmptySource
    @ParameterizedTest(name = "url = {0}")
    void createParty_EMPTY_IMAGE(String emptyImageUrl) {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, emptyImageUrl, "message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(PartyCreateRequest.class))).willReturn(Mono.just(mockResponse));

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "사진 URL이 비어 있습니다!");
    }



    @DisplayName("직관팟 생성 중 소개 문구는 필수이다.")
    @NullAndEmptySource
    @ParameterizedTest(name = "message = {0}")
    void createParty_EMPTY_MESSAGE(String emptyMessage) {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, "url",emptyMessage);
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(PartyCreateRequest.class))).willReturn(Mono.just(mockResponse));

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "직관팟 소개 문구가 비어 있습니다!");
    }

    private WebTestClient.BodySpec<String, ?> assertBadRequestOfPartyCreateRequest(PartyCreateRequest request, String requestUri, String expectedResult) {
        return webTestClient.post()
                .uri(requestUri)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .value(body -> {
                    assertThat(body).isEqualTo(expectedResult);
                });
    }
}
