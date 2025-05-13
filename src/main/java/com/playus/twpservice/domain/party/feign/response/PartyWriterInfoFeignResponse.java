package com.playus.twpservice.domain.party.feign.response;

import lombok.Builder;

@Builder
public record PartyWriterInfoFeignResponse(
        Long id,
        String writerName,
        String writerGender,
        String writerThumbnailUrl
) {

}
