package com.playus.twpservice.domain.party.dto.presigned;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PresignedUrlForSaveImageResponse {

    private String presignedUrl;

    public PresignedUrlForSaveImageResponse(String presignedUrl) {
        this.presignedUrl = presignedUrl;
    }
}
