package com.playus.twpservice.domain.party.specification;

import com.playus.twpservice.domain.party.dto.party_create.PartyCreateRequest;
import com.playus.twpservice.domain.party.dto.party_create.PartyCreateResponse;
import com.playus.twpservice.domain.party.dto.partybymatch.PartyInfoListByMatchRequest;
import com.playus.twpservice.domain.party.dto.partybymatch.PartyInfoResponse;
import com.playus.twpservice.domain.party.dto.presigned.PresignedUrlForSaveImageRequest;
import com.playus.twpservice.domain.party.dto.presigned.PresignedUrlForSaveImageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface PartyControllerSpecification {


    @Tag(name = "Post", description = "직관팟 생성 API")
    @Operation(
            summary = "직관팟 생성",
            description = "로그인한 사용자가 작성자로서 직관팟을 생성합니다.",
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
                      "thumbnailUrl": "https://image.example.com/party.jpg",
                      "matchId" : "1",
                      "message": "재밌게 응원할 분 구해요!"
                    }
                    """
                            )
                    )
            )
    )
    @ApiResponses(value = {
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
            )
    })
    ResponseEntity<PartyCreateResponse> createParty(Long userId, @Valid PartyCreateRequest request);

    @Tag(name = "Post", description = "Presigned URL 발급 API")
    @Operation(
            summary = "Presigned URL 발급",
            description = "프론트에서 이미지를 직접 저장하기 위한 Presigned URL을 생성 및 반환합니다. (버킷에 저장할 때는 PUT으로)",
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
            )
    })
    PresignedUrlForSaveImageResponse generatePresignedUrlForSaveImage(@Valid PresignedUrlForSaveImageRequest request);

    @Tag(name = "Get", description = "직관팟 조회 API")
    @Operation(
            summary = "직관팟 조회 API",
            description = "특정 경기에 대한 모든 직관팟을 조회합니다.",
            parameters = {
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
            )
    })
    List<PartyInfoResponse> getPartiesByMatchId(@Valid PartyInfoListByMatchRequest request);
}
