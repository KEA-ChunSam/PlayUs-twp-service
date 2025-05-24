package com.playus.twpservice.domain.common.feign.response;

import lombok.Builder;

import java.util.List;

@Builder
public record PartyUserThumbnailUrlListResponse(
     List<String> thumbnailUrls
) {

    public static PartyUserThumbnailUrlListResponse of(List<String> thumbnailUrls) {
        return PartyUserThumbnailUrlListResponse.builder()
                .thumbnailUrls(thumbnailUrls)
                .build();
    }

    public static PartyUserThumbnailUrlListResponse withServiceUnavailable() {
        return PartyUserThumbnailUrlListResponse.builder()
                .thumbnailUrls(List.of())
                .build();
    }
}
