package com.playus.twp_service.party.service;

import com.playus.twp_service.party.dto.party_create.PartyCreateRequest;
import com.playus.twp_service.party.dto.party_create.PartyCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PartyService {
    public Mono<PartyCreateResponse> createParty(PartyCreateRequest request) {
        return null;
    }
}
