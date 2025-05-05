package com.playus.twp_service.domain.party.controller;

import com.playus.twp_service.ControllerTestSupport;
import com.playus.twp_service.domain.party.dto.party_create.PartyCreateRequest;
import com.playus.twp_service.domain.party.dto.party_create.PartyCreateResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PartyControllerTest extends ControllerTestSupport {

    Long userId = 1L;
    String thumbnailUrl = "http://image.com";
    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userId, null, List.of(new SimpleGrantedAuthority("USER")));

    @DisplayName("직관팟을 생성할 수 있다.")
    @Test
    void createParty() throws Exception {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("title", "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, thumbnailUrl, "message");
        PartyCreateResponse response = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(response);

        // when // then
        mockMvc.perform(post("/party")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.partyId").value("1"))
                .andExpect(jsonPath("$.success").value("true"));
    }



    @DisplayName("직관팟 생성 중 제목은 필수이다.")
    @NullAndEmptySource
    @ParameterizedTest(name = "title = {0}")
    void createParty_EMPTY_TITLE(String emptyTitle) throws Exception {
        // given
        PartyCreateRequest request = PartyCreateRequest.of(emptyTitle, "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, thumbnailUrl,"message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "직관팟 제목이 비어 있습니다!");
    }

    @DisplayName("직관팟 생성 중 제목은 225자를 넘길 수 없다.")
    @Test
    void createParty_EXCEED_TITLE() throws Exception {
        String exceedTitle = "a".repeat(226);
        PartyCreateRequest request = PartyCreateRequest.of(exceedTitle, "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, thumbnailUrl,"message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "제목의 길이를 1~225자 이내로 작성해 주세요!");
    }




    @DisplayName("직관팟 생성 중 신청 방식은 필수이다.")
    @NullAndEmptySource
    @ParameterizedTest(name = "method = {0}")
    void createParty_EMPTY_METHOD(String emptyMethod) throws Exception {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("제목", emptyMethod, "남자만", List.of("10대", "20대"), 1L, 10L, thumbnailUrl,"message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "신청 방식이 비어 있습니다!");
    }

    @DisplayName("직관팟 생성 중 신청 방식은 정해진 양식대로 입력해야 한다.")
    @CsvSource(value = {"FIRST_COME", "RESERVATION", "ABCDEFG", "ㄱㄴㄷㄹㅁㅂㅅㅇ", "!@#$%^&", "123456789"})
    @ParameterizedTest(name = "method = {0}")
    void createParty_INVALID_METHOD(String invalidMethod) throws Exception {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("제목", invalidMethod, "남자만", List.of("10대", "20대"), 1L, 10L, thumbnailUrl,"message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "잘못된 신청 방식입니다!");
    }




    @DisplayName("직관팟 생성 중 참여 원하는 성별은 필수이다.")
    @NullAndEmptySource
    @ParameterizedTest(name = "gender = {0}")
    void createParty_EMPTY_GENDER(String emptyGender) throws Exception {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", emptyGender, List.of("10대", "20대"), 1L, 10L, thumbnailUrl,"message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "참여 원하는 성별이 비어 있습니다!");
    }

    @DisplayName("직관팟 생성 중 성별은 정해진 양식대로 입력해야 한다.")
    @CsvSource(value = {"MALE", "FEMALE", "NO_MATTER", "ㄱㄴㄷㄹㅁㅂㅅㅇ", "!@#$%^&", "123456789"})
    @ParameterizedTest(name = "method = {0}")
    void createParty_INVALID_GENDER(String invalidGender) throws Exception {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", invalidGender, List.of("10대", "20대"), 1L, 10L, thumbnailUrl,"message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "잘못된 성별 형식입니다!");
    }




    @DisplayName("직관팟 생성 중 참여자 나이는 필수이다.")
    @NullAndEmptySource
    @ParameterizedTest(name = "age = {0}")
    void createParty_EMPTY_AGE(List<String> emptyAgeList) throws Exception {

        // given
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", emptyAgeList, 1L, 10L, thumbnailUrl,"message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "참여자 나이가 비어 있습니다!");
    }

    @DisplayName("직관팟 생성 중 참여자 나이는 정해진 양식대로 입력해야 한다.")
    @MethodSource("invalidAgeGroupProvider")
    @ParameterizedTest(name = "age = {0}")
    void createParty_INVALID_AGE(List<String> emptyAgeList) throws Exception {

        // given
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", emptyAgeList, 1L, 10L, thumbnailUrl,"message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

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
    void createParty_TOO_MANY_AGE() throws Exception {

        // given
        List<String> tooManyAgeList = List.of("10대", "20대", "30대", "40대", "50대", "60대", "70대", "80대", "90대");
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", tooManyAgeList, 1L, 10L, thumbnailUrl,"message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "참여자 나이는 최대 6개까지 가능합니다!");
    }



    @DisplayName("직관팟 최소 인원은 필수이다.")
    @Test
    void createParty_EMPTY_MINIMUM() throws Exception {
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", List.of("10대", "20대"), null, 10L, thumbnailUrl,"message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "최소 참여 인원이 비어 있습니다!");
    }

    @DisplayName("직관팟 최소 인원은 1 이상이여야 한다.")
    @Test
    void createParty_INVALID_MINIMUM() throws Exception {
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", List.of("10대", "20대"), 0L, 10L, thumbnailUrl,"message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "최소 참여 인원은 1명 이상이여야 합니다!");
    }



    @DisplayName("직관팟 최대 인원은 필수이다.")
    @Test
    void createParty_EMPTY_MAXIMUM() throws Exception {
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", List.of("10대", "20대"), 1L, null, thumbnailUrl,"message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "최대 참여 인원이 비어 있습니다!");
    }

    @DisplayName("직관팟 최소 인원은 최대 인원보다 클 수 없다.")
    @Test
    void createParty_MINIMUM_MAXIMUM() throws Exception {
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", List.of("10대", "20대"), 10L, 9L, thumbnailUrl,"message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "최소 참여 인원은 최대 참여 인원보다 클 수 없습니다!");
    }



    @DisplayName("직관팟 생성 중 사진 url은 http/https/ftp로 시작해야 한다")
    @CsvSource(value = {"ftp://example.com", "http://example.com", "https://example.com", "ftp://example.com"})
    @ParameterizedTest(name = "url = {0}")
    void createParty_VALID_IMAGE_URL(String validUrl) throws Exception {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("title", "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, validUrl, "message");
        PartyCreateResponse response = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(response);

        // when // then
        mockMvc.perform(post("/party")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.partyId").value("1"))
                .andExpect(jsonPath("$.success").value("true"));
    }

    @DisplayName("직관팟 생성 중 사진 url은 올바른 format이여야 한다.")
    @CsvSource(value = {"abc.com", "www.naver.com", "abc@123.com", "123456788", "htttps://example.com"})
    @ParameterizedTest(name = "url = {0}")
    void createParty_INVALID_IMAGE_URL(String invalidUrl) throws Exception {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("title", "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, invalidUrl, "message");
        PartyCreateResponse response = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(response);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "올바른 URL 형식이 아닙니다!");
    }







    @DisplayName("직관팟 생성 중 소개 문구는 필수이다.")
    @NullAndEmptySource
    @ParameterizedTest(name = "message = {0}")
    void createParty_EMPTY_MESSAGE(String emptyMessage) throws Exception {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, thumbnailUrl,emptyMessage);
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "직관팟 소개 문구가 비어 있습니다!");
    }

    @DisplayName("직관팟 생성 중 소개 문구는 100자 이내여야 한다.")
    @Test
    void createParty_EXCEED_MESSAGE() throws Exception {
        // given
        String exceedMessage = "a".repeat(101);
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, thumbnailUrl, exceedMessage);
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "직관팟 소개 문구의 길이를 1~100자 이내로 작성해 주세요!");
    }

    private void assertBadRequestOfPartyCreateRequest(PartyCreateRequest request, String requestUri, String expectedResult) throws Exception {
        String responseBody = mockMvc.perform(post(requestUri)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(responseBody).isEqualTo(expectedResult);
    }
}
