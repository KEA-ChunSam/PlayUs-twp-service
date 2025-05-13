package com.playus.twpservice.domain.party.feign.response;

import lombok.Builder;

import java.util.List;

@Builder
public record PartyUserThumbnailUrlsResponse(
     List<String> thumbnailUrls
) {
}
