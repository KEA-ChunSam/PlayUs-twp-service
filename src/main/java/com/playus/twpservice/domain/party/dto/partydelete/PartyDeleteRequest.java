package com.playus.twpservice.domain.party.dto.partydelete;

import jakarta.validation.constraints.Min;
import lombok.Builder;

@Builder
public record PartyDeleteRequest(
        @Min(value = 1, message = "직관팟 작성자의 ID 는 1 이상이여야 합니다!")
        Long writerId
) {
    public static PartyDeleteRequest of(Long writerId) {
        return PartyDeleteRequest.builder()
                .writerId(writerId)
                .build();
    }
}
