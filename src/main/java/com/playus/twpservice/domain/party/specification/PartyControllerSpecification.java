package com.playus.twpservice.domain.party.specification;

import com.playus.twpservice.domain.common.security.CustomOAuth2User;
import com.playus.twpservice.domain.party.dto.applieduser.PartyAppliedUserResponse;
import com.playus.twpservice.domain.party.dto.apply.PartyApplyResponse;
import com.playus.twpservice.domain.party.dto.apply.PartyApproveApplyRequest;
import com.playus.twpservice.domain.party.dto.approve.PartyApproveRequest;
import com.playus.twpservice.domain.party.dto.approve.PartyApproveResponse;
import com.playus.twpservice.domain.party.dto.create.PartyCreateRequest;
import com.playus.twpservice.domain.party.dto.create.PartyCreateResponse;
import com.playus.twpservice.domain.party.dto.delete.PartyDeleteResponse;
import com.playus.twpservice.domain.party.dto.detail.PartyDetailRequest;
import com.playus.twpservice.domain.party.dto.detail.PartyDetailResponse;
import com.playus.twpservice.domain.party.dto.info.PartyInfoRequest;
import com.playus.twpservice.domain.party.dto.info.PartyInfoResponse;
import com.playus.twpservice.domain.common.request.PartyIdRequest;
import com.playus.twpservice.domain.party.dto.update.PartyUpdateRequest;
import com.playus.twpservice.domain.party.dto.update.PartyUpdateResponse;
import com.playus.twpservice.domain.party.dto.presigned.PresignedUrlForSaveImageRequest;
import com.playus.twpservice.domain.party.dto.presigned.PresignedUrlForSaveImageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface PartyControllerSpecification {


    @Tag(name = "Post", description = "직관팟 생성 API")
    @Operation(
            summary = "직관팟 생성",
            description = "로그인한 사용자가 작성자로서 직관팟을 생성합니다.",
            security = @SecurityRequirement(name = "Access"),
            parameters = @Parameter(
                    name = "Access",
                    description = "JWT Access Token (쿠키)",
                    in = ParameterIn.COOKIE,
                    required = true,
                    example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
            ),
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "직관팟 생성 요청 예시",
                                    value = """
                                            {
                                              "title": "롯데 vs LG 직관 같이 가요!",
                                              "partyJoinMethod": "선착순",
                                              "partyGender": "남자만",
                                              "ageGroup": ["10대", "20대"],
                                              "minimumParticipants": 3,
                                              "maximumParticipants": 5,
                                              "thumbnailImageNameList": ["party.jpg"],
                                              "matchId" : "1",
                                              "message": "재밌게 응원할 분 구해요!"
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201", description = "생성 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "직관팟 생성 응답 예시",
                                    value = """
                                            {
                                              "partyId": 1,
                                              "chatRoomId": "chatRoomId"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "최소 참여 인원이 최대 참여 인원보다 클 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "최소 참여 인원은 최대 참여 인원보다 클 수 없습니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "직관팟 제목이 비어 있을 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 제목이 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 제목이 1~225자 범위 이외일 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "제목의 길이를 1~225자 이내로 작성해 주세요!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "직관팟 신청 방식이 비어 있을 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "신청 방식이 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 신청 방식이 기존에 정의되어 있던 것이 아닐 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "잘못된 신청 방식입니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "직관팟 생성 시 성별이 비어 있을 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "참여 원하는 성별이 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 생성 시 성별이 기존에 정의되어 있던 것이 아닐 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "잘못된 성별 형식입니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "직관팟 생성 시 참여자 나이가 비어 있을 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "참여자 나이가 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 생성 시 참여자 나이가 기존에 정의되어 있던 것이 아닐 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "잘못된 참여자 나이입니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 생성 시 참여자 나이 개수가 기존에 정의되어 있던 나이를 초과했을 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "참여자 나이는 최대 6개까지 가능합니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "최소 참여 인원이 비어있을 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "최소 참여 인원이 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "최소 참여 인원이 1명 아래일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "최소 참여 인원은 1명 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "최대 참여 인원이 비어있을 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "최대 참여 인원이 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "최대 참여 인원이 1명 아래일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "최대 참여 인원은 1명 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "썸네일 이미지가 10개를 넘어갈 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "썸네일은 최대 10개까지만 가능합니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "썸네일 이미지명이 빈 문자열일 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "사진 파일명이 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "경기 ID가 비어있을 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "경기 ID는 필수입니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "경기 ID가 1 미만일 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "경기 ID는 1 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "직관팟 소개 문구가 비어있을 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 소개 문구가 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 소개 문구가 1~100자 사이가 아닐 경우",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 소개 문구의 길이를 1~100자 이내로 작성해 주세요!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "유효하지 않은 토큰입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 500,
                                              "status": "INTERNAL_SERVER_ERROR",
                                              "message": "서버 내부 오류가 발생했습니다. 관리자에게 문의해 주세요."
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<PartyCreateResponse> createParty(@Parameter(hidden = true) CustomOAuth2User principal,
                                                    @Valid @Parameter(description = "직관팟 생성 요청", required = true) PartyCreateRequest request);

    @Tag(name = "Post", description = "Presigned URL 발급 API")
    @Operation(
            summary = "Presigned URL 발급",
            description = "프론트에서 이미지를 직접 저장하기 위한 Presigned URL을 생성 및 반환합니다. (버킷에 저장할 때는 PUT으로)",
            security = @SecurityRequirement(name = "Access"),
            parameters = @Parameter(
                    name = "Access",
                    description = "JWT Access Token (쿠키)",
                    in = ParameterIn.COOKIE,
                    required = true,
                    example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
            ),
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Presigned URL 발급 요청 예시",
                                    value = """
                                            {
                                              "imageFileName": "party_thumbnail.jpg"
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "발급 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Presigned URL 발급 응답 예시",
                                    value = """
                                            {
                                              "presignedUrl": "http://presigned-url.com"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "이미지 파일명이 비어있을 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "이미지 파일명은 필수입니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "유효하지 않은 토큰입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 500,
                                              "status": "INTERNAL_SERVER_ERROR",
                                              "message": "서버 내부 오류가 발생했습니다. 관리자에게 문의해 주세요."
                                            }
                                            """
                            )
                    )
            )
    })
    PresignedUrlForSaveImageResponse generatePresignedUrlForSaveImage(@Valid @Parameter(description = "Presigned URL 발급 요청", required = true)
                                                                      PresignedUrlForSaveImageRequest request);

    @Tag(name = "Get", description = "직관팟 조회 API")
    @Operation(
            summary = "직관팟 조회 API",
            description = "특정 경기에 대한 모든 직관팟을 조회합니다.",
            security = @SecurityRequirement(name = "Access"),
            parameters = {
                    @Parameter(
                            name = "Access",
                            description = "JWT Access Token (쿠키)",
                            in = ParameterIn.COOKIE,
                            required = true,
                            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    ),
                    @Parameter(
                            name = "matchId",
                            description = "경기 ID",
                            required = true,
                            example = "1"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "직관팟 조회 응답 예시",
                                    value = """
                                                                    [
                                                                       {
                                                                         "partyId": 1,
                                                                         "title": "title",
                                                                         "partyJoinMethod": "승인제",
                                                                         "partyAges": ["10대", "20대"],
                                                                         "availableGender": "남자만",
                                                                         "authorName": "ZSJ",
                                                                         "authorGender": "남성",
                                                                         "matchDate": "3.22(토) 오후 2:00",
                                                                         "currentParticipantsCount": 10,
                                                                         "maximumParticipantsCount": 14,
                                                                         "partyThumbnailUrls": [
                                                                           "http://party-thumbnail",
                                                                           "http://party-thumbnail2"
                                                                         ],
                                                                         "userThumbnailUrls": [
                                                                           "http://user-thumbnailUrl",
                                                                           "http://user2-thumbnailUrl"
                                                                         ]
                                                                       }, 
                                            
                                                                          {
                                                                             "partyId": 2,
                                                                             "title": "title2",
                                                                             "partyJoinMethod": "선착순",
                                                                             "partyAges": ["30대"],
                                                                             "availableGender": "여자만",
                                                                             "authorName": "ZSJ",
                                                                             "authorGender": "남성",
                                                                             "matchDate": "3.22(토) 오후 2:00",
                                                                             "currentParticipantsCount": 1,
                                                                             "maximumParticipantsCount": 5,
                                                                             "partyThumbnailUrls": [
                                            
                                                                             ],
                                                                             "userThumbnailUrls": [
                                            
                                                                             ]
                                                                          }
                                                                     ]
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "경기 ID가 없을 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "경기 ID는 필수입니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "경기 ID가 1 미만일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "ID는 1 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "유효하지 않은 토큰입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 500,
                                              "status": "INTERNAL_SERVER_ERROR",
                                              "message": "서버 내부 오류가 발생했습니다. 관리자에게 문의해 주세요."
                                            }
                                            """
                            )
                    )
            )
    })
    List<PartyInfoResponse> getPartiesByMatchId(@Valid @Parameter(description = "직관팟 리스트 요청", required = true) PartyInfoRequest request);

    @Tag(name = "Get", description = "직관팟 상세정보 조회 API")
    @Operation(
            summary = "직관팟 상세정보 조회 API",
            description = "특정 직관팟에 대한 자세한 정보를 조회합니다.",
            parameters = {
                    @Parameter(
                            name = "Access",
                            description = "JWT Access Token (쿠키)",
                            in = ParameterIn.COOKIE,
                            required = true,
                            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    ),
                    @Parameter(
                            name = "partyId",
                            in = ParameterIn.PATH,
                            description = "직관팟 ID",
                            required = true,
                            example = "1"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "직관팟 상세정보 조회 응답 예시",
                                    value = """
                                            {
                                              "partyId": 1,
                                              "title": "title",
                                              "partyJoinMethod": "승인제",
                                              "text": "explanation",
                                              "partyAges": [
                                                "10대",
                                                "20대"
                                              ],
                                              "availableGender": "남자만",
                                              "authorName": "ZSJ",
                                              "authorGender": "남성",
                                              "matchDate": "3.22(토) 오후 2:00",
                                              "currentParticipantsCount": 10,
                                              "maximumParticipantsCount": 14,
                                              "partyThumbnailUrls": [
                                                "http://party-thumbnail",
                                                "http://party-thumbnail2.com"
                                              ],
                                              "userThumbnailUrls": [
                                                "http://user-thumbnailUrl",
                                                "http://user2-thumbnailUrl"
                                              ]
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 ID가 1 미만일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 ID는 1 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "유효하지 않은 토큰입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "직관팟 ID로 직관팟을 찾을 수 없는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "직관팟이 존재하지 않습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 500,
                                              "status": "INTERNAL_SERVER_ERROR",
                                              "message": "서버 내부 오류가 발생했습니다. 관리자에게 문의해 주세요."
                                            }
                                            """
                            )
                    )
            )
    })
    PartyDetailResponse getPartyDetail(@Valid @Parameter(description = "직관팟 상세정보 요청", required = true) PartyDetailRequest request);


    @Tag(name = "Put", description = "직관팟 수정 API")
    @Operation(
            summary = "직관팟 수정",
            description = "직관팟 작성자인 로그인한 유저가 직관팟을 수정합니다.",
            parameters = {
                    @Parameter(
                            name = "Access",
                            description = "JWT Access Token (쿠키)",
                            in = ParameterIn.COOKIE,
                            required = true,
                            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    ),
                    @Parameter(
                            name = "partyId",
                            in = ParameterIn.PATH,
                            description = "직관팟 ID",
                            required = true,
                            example = "1"
                    )
            },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "직관팟 수정 요청 예시",
                                    value = """
                                            {
                                              "title": "롯데 vs LG 직관 같이 가요!",
                                              "writerId" : 1,
                                              "partyJoinMethod": "선착순",
                                              "partyGender": "남자만",
                                              "ageGroup": ["10대", "20대"],
                                              "minimumParticipants": 3,
                                              "maximumParticipants": 5,
                                              "thumbnailUrl": ["https://image.example.com/party.jpg"],
                                              "message": "재밌게 응원할 분 구해요!"
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "수정 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "직관팟 수정 응답 예시",
                                    value = """
                                            {
                                              "partyId": 1
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 ID가 1 미만일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 ID는 1 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "최소 참여 인원이 최대 참여 인원보다 클 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "최소 참여 인원은 최대 참여 인원보다 클 수 없습니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "직관팟 제목이 비어 있을 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 제목이 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 제목이 1~225자 범위 이외일 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "제목의 길이를 1~225자 이내로 작성해 주세요!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "작성자 ID가 1 미만일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "작성자 ID는 1 이상이어야 합니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "직관팟 신청 방식이 비어 있을 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "신청 방식이 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 신청 방식이 기존에 정의되어 있던 것이 아닐 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "잘못된 신청 방식입니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "직관팟 수정 시 성별이 비어 있을 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "참여 원하는 성별이 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 수정 시 성별이 기존에 정의되어 있던 것이 아닐 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "잘못된 성별 형식입니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "직관팟 수정 시 참여자 나이가 비어 있을 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "참여자 나이가 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 수정 시 참여자 나이가 기존에 정의되어 있던 것이 아닐 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "잘못된 참여자 나이입니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 수정 시 참여자 나이 개수가 기존에 정의되어 있던 나이를 초과했을 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "참여자 나이는 최대 6개까지 가능합니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "최소 참여 인원이 비어있을 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "최소 참여 인원이 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "최소 참여 인원이 1명 아래일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "최소 참여 인원은 1명 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "최대 참여 인원이 비어있을 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "최대 참여 인원이 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "최대 참여 인원이 1명 아래일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "최대 참여 인원은 1명 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "썸네일 이미지가 10개를 넘어갈 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "썸네일은 최대 10개까지만 가능합니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "썸네일 이미지명이 빈 문자열일 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "사진 파일명이 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "직관팟 소개 문구가 비어있을 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 소개 문구가 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 소개 문구가 1~100자 사이가 아닐 경우",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 소개 문구의 길이를 1~100자 이내로 작성해 주세요!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "로그인 유저가 직관팟 작성자가 아닐 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 작성자가 아니면 수정할 수 없습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "유효하지 않은 토큰입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "직관팟 ID로 직관팟을 찾을 수 없는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "잘못된 직관팟 번호입니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 500,
                                              "status": "INTERNAL_SERVER_ERROR",
                                              "message": "서버 내부 오류가 발생했습니다. 관리자에게 문의해 주세요."
                                            }
                                            """
                            )
                    )
            )
    })
    PartyUpdateResponse updateParty(@Parameter(hidden = true) CustomOAuth2User principal,
                                    @Valid @Parameter(description = "API 경로로 들어오는 직관팟 ID 위해 작성", required = true) PartyIdRequest idRequest,
                                    @Valid @Parameter(description = "직관팟 수정 요청", required = true) PartyUpdateRequest request);

    @Tag(name = "Patch", description = "직관팟 삭제 API")
    @Operation(
            summary = "직관팟 삭제",
            description = "직관팟 작성자인 로그인한 유저가 직관팟을 삭제합니다.",
            security = @SecurityRequirement(name = "Access"),
            parameters = {
                    @Parameter(
                            name = "Access",
                            description = "JWT Access Token (쿠키)",
                            in = ParameterIn.COOKIE,
                            required = true,
                            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    ),
                    @Parameter(
                            name = "partyId",
                            in = ParameterIn.PATH,
                            description = "직관팟 ID",
                            required = true,
                            example = "1"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "삭제 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "직관팟 삭제 응답 예시",
                                    value = """
                                            {
                                              "deletedPartyId": 1
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 ID가 1 미만일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 ID는 1 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "로그인 유저가 직관팟 작성자가 아닐 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 작성자가 아니면 수정할 수 없습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "유효하지 않은 토큰입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "직관팟 ID로 직관팟을 찾을 수 없는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "잘못된 직관팟 번호입니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 500,
                                              "status": "INTERNAL_SERVER_ERROR",
                                              "message": "서버 내부 오류가 발생했습니다. 관리자에게 문의해 주세요."
                                            }
                                            """
                            )
                    )
            )
    })
    PartyDeleteResponse deleteParty(@Parameter(hidden = true) CustomOAuth2User principal,
                                    @Valid @Parameter(description = "API 경로로 들어오는 직관팟 ID 위해 작성", required = true) PartyIdRequest idRequest);


    @Tag(name = "Post", description = "직관팟 선착순 신청 API")
    @Operation(
            summary = "직관팟 선착순 신청",
            description = "선착순 승인제인 직관팟에 신청합니다.",
            security = @SecurityRequirement(name = "Access"),
            parameters = {
                    @Parameter(
                            name = "Access",
                            description = "JWT Access Token (쿠키)",
                            in = ParameterIn.COOKIE,
                            required = true,
                            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    ),
                    @Parameter(
                            name = "partyId",
                            in = ParameterIn.PATH,
                            description = "직관팟 ID",
                            required = true,
                            example = "1"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "신청 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "승인제 직관팟 신청 응답 예시",
                                    value = """
                                            {
                                              "message": "직관팟 가입에 성공하셨습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 ID가 1 미만일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 ID는 1 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 작성자가 직관팟 지원할 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 작성자는 지원할 수 없습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "유효하지 않은 토큰입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403", description = "거절된 사용자가 다시 지원하려는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 403,
                                              "status": "FORBIDDEN",
                                              "message": "신청이 거절되었으면 다시 지원할 수 없습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "직관팟 ID로 직관팟을 찾을 수 없는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "잘못된 직관팟 번호입니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "채팅방 ID로 직관팟을 찾을 수 없는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "채팅방이 존재하지 않습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409", description = "직관팟 정원이 초과될 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 409,
                                              "status": "CONFLICT",
                                              "message": "직관팟 정원이 초과되었습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409", description = "이미 가입 (혹은 대기) 상태인 직관팟에 다시 신청할 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 409,
                                              "status": "CONFLICT",
                                              "message": "이미 가입된 직관팟입니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 500,
                                              "status": "INTERNAL_SERVER_ERROR",
                                              "message": "서버 내부 오류가 발생했습니다. 관리자에게 문의해 주세요."
                                            }
                                            """
                            )
                    )
            )
    })
    PartyApplyResponse applyPartyFCFS(@Parameter(hidden = true) CustomOAuth2User principal,
                                      @Valid @Parameter(description = "API 경로로 들어오는 직관팟 ID 위해 작성", required = true) PartyIdRequest idRequest);


    @Tag(name = "Post", description = "승인제 직관팟 신청 API")
    @Operation(
            summary = "직관팟 승인제 신청",
            description = "승인제 직관팟에 신청합니다.",
            security = @SecurityRequirement(name = "Access"),
            parameters = {
                    @Parameter(
                            name = "Access",
                            description = "JWT Access Token (쿠키)",
                            in = ParameterIn.COOKIE,
                            required = true,
                            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    ),
                    @Parameter(
                            name = "partyId",
                            in = ParameterIn.PATH,
                            description = "직관팟 ID",
                            required = true,
                            example = "1"
                    )
            },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "승인제 직관팟 신청 요청 예시",
                                    value = """
                                            {
                                              "requireMessage" : "직관팟 참여하고 싶습니다!"
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "신청 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "승인제 직관팟 신청 응답 예시",
                                    value = """
                                            {
                                              "message": "직관팟 가입에 성공하셨습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 ID가 1 미만일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 ID는 1 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 작성자가 직관팟 지원할 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 작성자는 지원할 수 없습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "유효하지 않은 토큰입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403", description = "거절된 사용자가 다시 지원하려는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "신청이 거절되었으면 다시 지원할 수 없습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "직관팟 ID로 직관팟을 찾을 수 없는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "잘못된 직관팟 번호입니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "채팅방 ID로 직관팟을 찾을 수 없는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "채팅방이 존재하지 않습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409", description = "직관팟 정원이 초과될 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 409,
                                              "status": "CONFLICT",
                                              "message": "직관팟 정원이 초과되었습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409", description = "이미 가입 (혹은 대기) 상태인 직관팟에 다시 신청할 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 409,
                                              "status": "CONFLICT",
                                              "message": "이미 가입된 직관팟입니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 500,
                                              "status": "INTERNAL_SERVER_ERROR",
                                              "message": "서버 내부 오류가 발생했습니다. 관리자에게 문의해 주세요."
                                            }
                                            """
                            )
                    )
            )
    })
    PartyApplyResponse applyParty(@Parameter(hidden = true) CustomOAuth2User principal,
                                  @Valid @Parameter(description = "API 경로로 들어오는 직관팟 ID 위해 작성", required = true) PartyIdRequest idRequest,
                                  @Valid @Parameter(description = "직관팟 신청 시 방장에게 보여줄 requireMessage 작성") PartyApproveApplyRequest request);


    @Tag(name = "Patch", description = "방장으로서 승인제 직관팟 신청 유저 승인 API")
    @Operation(
            summary = "승인제 직관팟 신청 승인/거절",
            description = "직관팟 신청자에 대해 승인 여부를 결정합니다.",
            security = @SecurityRequirement(name = "Access"),
            parameters = {
                    @Parameter(
                            name = "Access",
                            description = "JWT Access Token (쿠키)",
                            in = ParameterIn.COOKIE,
                            required = true,
                            example = "abcd"
                    ),
                    @Parameter(
                            name = "partyId",
                            in = ParameterIn.PATH,
                            description = "직관팟 ID",
                            required = true,
                            example = "1"
                    )
            },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "승인제 직관팟 승인 여부 결정",
                                    value = """
                                            {
                                              "applicantUserId": 1,
                                              "isApproved" : true
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "승인",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "승인제 직관팟 승인 응답 예시",
                                    value = """
                                            {
                                              "message": "직관팟 가입 신청 승인 성공했습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "200", description = "거부",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "승인제 직관팟 거부 응답 예시",
                                    value = """
                                            {
                                              "message": "직관팟 가입 신청 거절 성공했습니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "직관팟 ID가 1 미만일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 ID는 1 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "지원자 ID가 비어있는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "지원자 ID는 필수입니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "지원자 ID가 1 미만일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "지원자 ID는 1 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "지원자 승인 여부가 비어있을 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "승인 여부는 필수입니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "선착순 직관팟에 승인 요청 보낼 시 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "선착순 모집인 직관팟에는 승인 요청을 보낼 수 없습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 방장이 아닌 사람이 승인 API 날릴 시 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "작성자가 아니면 승인할 수 없습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "유효하지 않은 토큰입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401", description = "거절된 사용자가 다시 지원하려는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "신청이 거절되었으면 다시 지원할 수 없습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "직관팟 ID로 직관팟을 찾을 수 없는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "직관팟이 존재하지 않습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "이전에 지원한 기록이 없는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "직관팟 지원자가 아닙니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "채팅방을 찾을 수 없을 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "채팅방이 존재하지 않습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409", description = "직관팟 정원이 초과될 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 409,
                                              "status": "CONFLICT",
                                              "message": "직관팟 정원이 초과되었습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 500,
                                              "status": "INTERNAL_SERVER_ERROR",
                                              "message": "서버 내부 오류가 발생했습니다. 관리자에게 문의해 주세요."
                                            }
                                            """
                            )
                    )
            )
    })
    PartyApproveResponse approveParty(@Parameter(hidden = true) CustomOAuth2User principal,
                                      @Valid @Parameter(description = "API 경로로 들어오는 직관팟 ID 위해 작성", required = true) PartyIdRequest idRequest,
                                      @Valid @Parameter(description = "방장이 승인할 userId와 승인 여부 작성") PartyApproveRequest request);


    @Tag(name = "Get", description = "직관팟 신청 유저 조회 API")
    @Operation(
            summary = "직관팟 신청 유저 조회 API",
            description = "특정 승인제 직관팟에 대해 신청한 모든 유저를 조회합니다",
            security = @SecurityRequirement(name = "Access"),
            parameters = {
                    @Parameter(
                            name = "Access",
                            description = "JWT Access Token (쿠키)",
                            in = ParameterIn.COOKIE,
                            required = true,
                            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    ),
                    @Parameter(
                            name = "partyId",
                            in = ParameterIn.PATH,
                            description = "직관팟 ID",
                            required = true,
                            example = "1"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "직관팟 신청 유저 조회 응답 예시",
                                    value = """
                                                                    [
                                                                       {
                                                                         "userId": 1,
                                                                         "name": "ZSJ",
                                                                         "ageGroup": "20대",
                                                                         "thumbnailUrl" : "http://thumbnailUrl",
                                                                         "requireMessage" : "참여 희망합니다!"
                                                                       }, 
                                            
                                                                       {
                                                                         "userId": 2,
                                                                         "name": "KIM",
                                                                         "ageGroup": "30대",
                                                                         "thumbnailUrl" : "http://thumbnailUrl2",
                                                                         "requireMessage" : "같이 즐겨봐요!"
                                                                       }, 
                                                                     ]
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 ID가 1 미만일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 ID는 1 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 방장이 아닌 자가 직관팟 지원자 조회 시도할 경우",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "방장이 아니면 직관팟 지원자를 조회할 수 없습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "유효하지 않은 토큰입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "직관팟 ID로 직관팟을 찾을 수 없는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "직관팟이 존재하지 않습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 500,
                                              "status": "INTERNAL_SERVER_ERROR",
                                              "message": "서버 내부 오류가 발생했습니다. 관리자에게 문의해 주세요."
                                            }
                                            """
                            )
                    )
            )
    })
    List<PartyAppliedUserResponse> getAppliedUsers(@Parameter(hidden = true) CustomOAuth2User principal,
                                                   @Valid @Parameter(description = "API 경로로 들어오는 직관팟 ID 위해 작성", required = true) PartyIdRequest idRequest);
}
