package com.playus.twpservice.domain.party.repository.read.custom;

import com.playus.twpservice.domain.party.vo.PartyInfo;

import java.util.List;

public interface PartyReadOnlyRepositoryCustom {
    List<PartyInfo> findPartySummariesByMatchId(Long matchId);
}
