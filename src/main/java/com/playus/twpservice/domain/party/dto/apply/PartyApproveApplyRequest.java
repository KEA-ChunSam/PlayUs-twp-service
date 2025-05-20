package com.playus.twpservice.domain.party.dto.apply;

import lombok.Builder;

@Builder
public record PartyApproveApplyRequest (
        String requireMessage
) {
    public static PartyApproveApplyRequest of (String requireMessage) {
        return PartyApproveApplyRequest.builder()
                .requireMessage(requireMessage)
                .build();
    }
}
