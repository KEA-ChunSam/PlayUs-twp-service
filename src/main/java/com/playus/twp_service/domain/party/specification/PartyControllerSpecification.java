package com.playus.twp_service.domain.party.specification;

import com.playus.twp_service.domain.party.dto.party_create.PartyCreateRequest;
import com.playus.twp_service.domain.party.dto.party_create.PartyCreateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;

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
                      "method": "선착순",
                      "gender": "남자만",
                      "ageGroup": ["10대", "20대"],
                      "minimumParticipants": 3,
                      "maximumParticipants": 5,
                      "thumbnailUrl": "https://image.example.com/party.jpg",
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
                      "success": true
                    }
                    """
                            )
                    )
            )
    })
    ResponseEntity<PartyCreateResponse> createParty(Long userId, PartyCreateRequest request);
}
