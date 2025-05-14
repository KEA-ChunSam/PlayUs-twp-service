package com.playus.twpservice.domain.party.repository.read.custom;

import com.playus.twpservice.domain.party.vo.PartyInfo;

import java.util.List;
import java.util.Optional;

public interface PartyReadOnlyRepositoryCustom {
    List<PartyInfo> findPartyInfoBy(Long matchId);
    Optional<PartyInfo> findPartyDetailBy(Long partyId);
}
