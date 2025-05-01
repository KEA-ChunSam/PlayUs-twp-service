package com.playus.twp_service.party.controller;

import com.playus.twp_service.party.dto.party_create.PartyCreateRequest;
import com.playus.twp_service.party.dto.party_create.PartyCreateResponse;
import com.playus.twp_service.party.service.PartyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/party")
@RequiredArgsConstructor
public class PartyController {

    private final PartyService partyService;

    @PostMapping
    public Mono<ResponseEntity<PartyCreateResponse>> createParty(@Valid @RequestBody PartyCreateRequest request) {
        return partyService.createParty(request)
                .map(partyCreateResponse -> ResponseEntity.status(HttpStatus.CREATED).body(partyCreateResponse));
    }
}
