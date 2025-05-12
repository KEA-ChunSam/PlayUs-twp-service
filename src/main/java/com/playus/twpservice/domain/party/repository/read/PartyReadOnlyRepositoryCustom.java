package com.playus.twpservice.domain.party.repository.read;

import com.playus.twpservice.domain.party.vo.PartySummary;

import java.util.List;

public interface PartyReadOnlyRepositoryCustom {
    List<PartySummary> findPartySummariesByMatchId(Long matchId);
}
