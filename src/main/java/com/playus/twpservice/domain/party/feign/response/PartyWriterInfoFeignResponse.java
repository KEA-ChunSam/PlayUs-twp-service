package com.playus.twpservice.domain.party.feign.response;

import lombok.Builder;

@Builder
public record PartyWriterInfoFeignResponse(
        Long id,
        String writerName,
        String writerGender,
        String writerThumbnailUrl
) {

    public static PartyWriterInfoFeignResponse of (Long id, String writerName,
                                                   String writerGender, String writerThumbnailUrl) {
        return PartyWriterInfoFeignResponse.builder()
                .id(id)
                .writerName(writerName)
                .writerGender(writerGender)
                .writerThumbnailUrl(writerThumbnailUrl)
                .build();
    }

    public static PartyWriterInfoFeignResponse withServiceUnavailable() {
        return PartyWriterInfoFeignResponse.builder()
                .id(null)
                .writerName(null)
                .writerGender(null)
                .writerThumbnailUrl(null)
                .build();
    }
}
