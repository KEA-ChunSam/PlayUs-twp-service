package com.playus.twpservice.domain.party.dto.partydelete;

import lombok.Builder;

@Builder
public record PartyDeleteResponse(
    Long deletedPartyId
) {

}
