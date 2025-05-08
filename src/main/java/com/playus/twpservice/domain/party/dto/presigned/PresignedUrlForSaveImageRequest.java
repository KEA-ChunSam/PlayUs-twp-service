package com.playus.twpservice.domain.party.dto.presigned;

import jakarta.validation.constraints.NotBlank;


public record PresignedUrlForSaveImageRequest (
        @NotBlank(message = "이미지 파일명은 필수입니다!")
        String imageFileName
) {

}
