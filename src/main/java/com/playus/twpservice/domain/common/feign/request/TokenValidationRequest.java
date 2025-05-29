package com.playus.twpservice.domain.common.feign.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record TokenValidationRequest(
        @NotBlank(message = "토큰은 필수 입력값입니다")
        String token
) {

     public static TokenValidationRequest of (String token) {
         return TokenValidationRequest.builder()
                 .token(token)
                 .build();
     }
}
