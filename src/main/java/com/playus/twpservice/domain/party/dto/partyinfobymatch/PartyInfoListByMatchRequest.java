package com.playus.twpservice.domain.party.dto.partyinfobymatch;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PartyInfoListByMatchRequest(
        @NotNull(message = "경기 ID는 필수입니다!")
        @Min(value = 1, message = "ID는 1 이상이여아 합니다!")
        Long matchId
) {
}
