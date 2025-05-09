package com.playus.twpservice.domain.party.controller;

import com.playus.twpservice.ControllerTestSupport;
import com.playus.twpservice.domain.party.dto.party_create.PartyCreateRequest;
import com.playus.twpservice.domain.party.dto.party_create.PartyCreateResponse;
import com.playus.twpservice.domain.party.dto.presigned.PresignedUrlForSaveImageRequest;
import com.playus.twpservice.domain.party.dto.presigned.PresignedUrlForSaveImageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PartyControllerTest extends ControllerTestSupport {

    Long userId = 1L;
    List<String> thumbnailUrl = List.of("http://image.com");
    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userId, null, List.of(new SimpleGrantedAuthority("USER")));

    //  직관팟 생성 happy case
    @DisplayName("직관팟을 생성할 수 있다.")
    @Test
    void createParty() throws Exception {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("title", "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, thumbnailUrl, 1L, "message");
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

    //  직관팟 생성 title 이슈
    @DisplayName("직관팟 생성 중 제목은 필수이다.")
    @NullAndEmptySource
    @ParameterizedTest(name = "title = {0}")
    void createParty_EMPTY_TITLE(String emptyTitle) throws Exception {
        // given
        PartyCreateRequest request = PartyCreateRequest.of(emptyTitle, "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, thumbnailUrl, 1L, "message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "직관팟 제목이 비어 있습니다!");
    }

    @DisplayName("직관팟 생성 중 제목은 225자를 넘길 수 없다.")
    @Test
    void createParty_EXCEED_TITLE() throws Exception {
        String exceedTitle = "a".repeat(226);
        PartyCreateRequest request = PartyCreateRequest.of(exceedTitle, "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, thumbnailUrl, 1L, "message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "제목의 길이를 1~225자 이내로 작성해 주세요!");
    }

    //  직관팟 생성 partyJoinMethod 이슈
    @DisplayName("직관팟 생성 중 신청 방식은 필수이다.")
    @NullAndEmptySource
    @ParameterizedTest(name = "partyJoinMethod = {0}")
    void createParty_EMPTY_METHOD(String emptyMethod) throws Exception {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("제목", emptyMethod, "남자만", List.of("10대", "20대"), 1L, 10L, thumbnailUrl, 1L, "message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "신청 방식이 비어 있습니다!");
    }

    @DisplayName("직관팟 생성 중 신청 방식은 정해진 양식대로 입력해야 한다.")
    @CsvSource(value = {"FIRST_COME", "RESERVATION", "ABCDEFG", "ㄱㄴㄷㄹㅁㅂㅅㅇ", "!@#$%^&", "123456789"})
    @ParameterizedTest(name = "partyJoinMethod = {0}")
    void createParty_INVALID_METHOD(String invalidMethod) throws Exception {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("제목", invalidMethod, "남자만", List.of("10대", "20대"), 1L, 10L, thumbnailUrl, 1L, "message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "잘못된 신청 방식입니다!");
    }

    //  직관팟 생성 partyGender 이슈
    @DisplayName("직관팟 생성 중 참여 원하는 성별은 필수이다.")
    @NullAndEmptySource
    @ParameterizedTest(name = "partyGender = {0}")
    void createParty_EMPTY_GENDER(String emptyGender) throws Exception {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", emptyGender, List.of("10대", "20대"), 1L, 10L, thumbnailUrl, 1L, "message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "참여 원하는 성별이 비어 있습니다!");
    }

    @DisplayName("직관팟 생성 중 성별은 정해진 양식대로 입력해야 한다.")
    @CsvSource(value = {"MALE", "FEMALE", "NO_MATTER", "ㄱㄴㄷㄹㅁㅂㅅㅇ", "!@#$%^&", "123456789"})
    @ParameterizedTest(name = "partyGender = {0}")
    void createParty_INVALID_GENDER(String invalidGender) throws Exception {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", invalidGender, List.of("10대", "20대"), 1L, 10L, thumbnailUrl, 1L, "message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "잘못된 성별 형식입니다!");
    }

    @DisplayName("직관팟 생성 중 성별은 남자만, 여자만, 상관없음 중 하나만 입력해아 한다.")
    @CsvSource(value = {"남자만", "여자만", "상관없음"})
    @ParameterizedTest(name = "partyGender = {0}")
    void createParty_VALID_GENDER(String gender) throws Exception {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("title", "선착순", gender, List.of("10대", "20대"), 1L, 10L, thumbnailUrl, 1L, "message");
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

    //  직관팟 생성 ageGroup 이슈
    @DisplayName("직관팟 생성 중 참여자 나이는 필수이다.")
    @NullAndEmptySource
    @ParameterizedTest(name = "age = {0}")
    void createParty_EMPTY_AGE(List<String> emptyAgeList) throws Exception {

        // given
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", emptyAgeList, 1L, 10L, thumbnailUrl, 1L, "message");
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
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", emptyAgeList, 1L, 10L, thumbnailUrl, 1L, "message");
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
        List<String> tooManyAgeList = List.of("10대", "20대", "30대", "40대", "50대", "60대 이상", "70대", "80대", "90대");
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", tooManyAgeList, 1L, 10L, thumbnailUrl, 1L, "message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "참여자 나이는 최대 6개까지 가능합니다!");
    }

    //  직관팟 생성 minimumParticipants 이슈
    @DisplayName("직관팟 최소 인원은 필수이다.")
    @Test
    void createParty_EMPTY_MINIMUM() throws Exception {
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", List.of("10대", "20대"), null, 10L, thumbnailUrl, 1L, "message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "최소 참여 인원이 비어 있습니다!");
    }

    @DisplayName("직관팟 최소 인원은 1 이상이여야 한다.")
    @Test
    void createParty_INVALID_MINIMUM() throws Exception {
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", List.of("10대", "20대"), 0L, 10L, thumbnailUrl, 1L, "message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "최소 참여 인원은 1명 이상이여야 합니다!");
    }

    //  직관팟 생성 maximumParticipants 이슈
    @DisplayName("직관팟 최대 인원은 필수이다.")
    @Test
    void createParty_EMPTY_MAXIMUM() throws Exception {
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", List.of("10대", "20대"), 1L, null, thumbnailUrl, 1L, "message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "최대 참여 인원이 비어 있습니다!");
    }

    @DisplayName("직관팟 최소 인원은 최대 인원보다 클 수 없다.")
    @Test
    void createParty_MINIMUM_MAXIMUM() throws Exception {
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", List.of("10대", "20대"), 10L, 9L, thumbnailUrl, 1L, "message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "최소 참여 인원은 최대 참여 인원보다 클 수 없습니다!");
    }

    //  직관팟 생성 thumbnailUrl 이슈
    @DisplayName("직관팟 생성 중 사진 url은 비어 있거나, http/https/ftp로 시작해야 한다.")
    @MethodSource("validUrlGroupProvider")
    @ParameterizedTest(name = "url = {0}")
    void createParty_VALID_IMAGE_URL(List<String> validUrl) throws Exception {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("title", "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, validUrl, 1L, "message");
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

    static Stream<Arguments> validUrlGroupProvider() {
        return Stream.of(
                Arguments.of(List.of()),
                Arguments.of(List.of("http://image.com")),
                Arguments.of(List.of("https://image.com")),
                Arguments.of(Arrays.asList("http://image.com", "https://image.com")),
                Arguments.of(Arrays.asList("http://image.co.kr", "https://image.com", "ftp://image.com"))
        );
    }

    @DisplayName("직관팟 생성 중 사진 url은 최대 10개까지만 담을 수 있다.")
    @Test
    void createParty_NULL_IMAGE_URL() throws Exception {
        // given
        List<String> tooManyUrlList = List.of("http://image.com", "https://image.com", "ftp://image.com", "http://image.com",
                "https://image.com", "ftp://image.com", "ftp://image.com", "http://image.com",
                "https://image.com", "ftp://image.com", "http://image.com");
        PartyCreateRequest request = PartyCreateRequest.of("title", "선착순", "남자만", List.of("10대", "20대"),
                1L, 10L, tooManyUrlList, 1L, "message");
        PartyCreateResponse response = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(response);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "썸네일은 최대 10개까지만 가능합니다!");
    }

    @DisplayName("직관팟 생성 중 사진 url은 올바른 format이여야 한다.")
    @MethodSource("invalidUrlGroupProvider")
    @ParameterizedTest(name = "url = {0}")
    void createParty_INVALID_IMAGE_URL(List<String> invalidUrl) throws Exception {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("title", "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, invalidUrl, 1L, "message");
        PartyCreateResponse response = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(response);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "올바른 URL 형식이 아닙니다!");
    }

    static Stream<Arguments> invalidUrlGroupProvider() {
        return Stream.of(
                Arguments.of(List.of("abc://123.com", "123456")),
                Arguments.of(List.of("!@#$%%", "#$%^&%$#", "#(*##$#$")),
                Arguments.of(Arrays.asList("사진 URL", "IMAGE URL")),
                Arguments.of(Arrays.asList("abc@123.com", "www.abc.com"))
        );
    }

    //  직관팟 생성 matchId 이슈
    @DisplayName("직관팟 생성 시 경기 ID는 필수이다.")
    @Test
    void createParty_EMPTY_MATCH_ID() throws Exception {
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", List.of("10대", "20대"),
                1L, 10L, thumbnailUrl, null, "message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "경기 ID는 필수입니다!");
    }

    @DisplayName("직관팟 생성 시 경기 ID는 1 이상이여야 한다.")
    @CsvSource(value = {"0", "-1", "-100", "-1000", "-10000"})
    @ParameterizedTest(name = "invalidMatchId = {0}")
    void createParty_INVALID_MATCH_ID(String invalidMatchIdStr) throws Exception {
        Long invalidMatchId = Long.valueOf(invalidMatchIdStr);
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", List.of("10대", "20대"),
                1L, 10L, thumbnailUrl, invalidMatchId, "message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "경기 ID는 1 이상이어야 합니다!");
    }

    //  직관팟 생성 message 이슈
    @DisplayName("직관팟 생성 중 소개 문구는 필수이다.")
    @NullAndEmptySource
    @ParameterizedTest(name = "message = {0}")
    void createParty_EMPTY_MESSAGE(String emptyMessage) throws Exception {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, thumbnailUrl, 1L, emptyMessage);
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
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, thumbnailUrl, 1L, exceedMessage);
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L, true);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "/party", "직관팟 소개 문구의 길이를 1~100자 이내로 작성해 주세요!");
    }

    @DisplayName("이미지 저장을 위한 Presigned URL을 발급해줄 수 있다.")
    @Test
    void generatePresignedUrlForSaveImage() throws Exception {
        // given
        PresignedUrlForSaveImageRequest request = new PresignedUrlForSaveImageRequest("image.jpg");
        given(partyService.generatePresignedUrlForSaveImage(any(PresignedUrlForSaveImageRequest.class)))
                .willReturn(new PresignedUrlForSaveImageResponse("https://presigned-url.com"));

        // when // then
        mockMvc.perform(post("/party/presigned-url")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.presignedUrl").value("https://presigned-url.com"));
    }

    @DisplayName("URL 발급을 위해서 이미지 파일명은 필수이다.")
    @NullAndEmptySource
    @ParameterizedTest(name = "url = {0}")
    void generatePresignedUrlForSaveImage_BLANK_IMAGE_NAME(String blankImageFileName) throws Exception {
        // given
        PresignedUrlForSaveImageRequest request = new PresignedUrlForSaveImageRequest(blankImageFileName);

        // when // then
        mockMvc.perform(post("/party/presigned-url")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("이미지 파일명은 필수입니다!"));
    }

    private void assertBadRequestOfPartyCreateRequest(PartyCreateRequest request, String requestUri, String expectedResult) throws Exception {
        mockMvc.perform(post(requestUri)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value(expectedResult));
    }
}
