package com.playus.twpservice.domain.party.service;

import com.playus.twpservice.domain.party.dto.partybymatch.PartiesByMatchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartyReadOnlyService {

    public PartiesByMatchResponse getPartiesBy(Long matchId, Long userId) {
        return null;
    }
}
