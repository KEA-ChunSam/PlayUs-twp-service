package com.playus.twpservice.domain.party.feign.response;

import lombok.Builder;

@Builder
public record PartyWriterInfoFeignResponse(
        Long id,
        String writerName,
        String writerGender,
        int writerAge,
        String writerThumbnailUrl
) {

    public static PartyWriterInfoFeignResponse of (Long id, String writerName,
                                                   String writerGender, int writerAge, String writerThumbnailUrl) {
        return PartyWriterInfoFeignResponse.builder()
                .id(id)
                .writerName(writerName)
                .writerGender(writerGender)
                .writerAge(writerAge)
                .writerThumbnailUrl(writerThumbnailUrl)
                .build();
    }

    public static PartyWriterInfoFeignResponse withServiceUnavailable() {
        return PartyWriterInfoFeignResponse.builder()
                .id(null)
                .writerName(null)
                .writerGender(null)
                .writerAge(-1)
                .writerThumbnailUrl(null)
                .build();
    }
}
