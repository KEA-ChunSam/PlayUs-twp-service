package com.playus.twpservice.domain.party.dto.approve;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record PartyApproveRequest (

        @NotNull(message = "지원자 ID는 필수입니다!")
        @Min(value = 1, message = "지원자 ID는 1 이상이여야 합니다!")
        Long applicantUserId,

        @NotNull(message = "승인 여부는 필수입니다!")
        Boolean isApproved
) {

    public static PartyApproveRequest of(Long applicantUserId, Boolean isApproved) {
        return PartyApproveRequest.builder()
                .applicantUserId(applicantUserId)
                .isApproved(isApproved)
                .build();
    }
}
