package com.playus.twp_service.domain.party.controller;

import com.playus.twp_service.domain.party.dto.party_create.PartyCreateRequest;
import com.playus.twp_service.domain.party.dto.party_create.PartyCreateResponse;
import com.playus.twp_service.domain.party.service.PartyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/party")
@RequiredArgsConstructor
public class PartyController {

    private final PartyService partyService;

    @PostMapping
    public ResponseEntity<PartyCreateResponse> createParty(@AuthenticationPrincipal Long userId, @Valid @RequestBody PartyCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(partyService.createParty(userId, request));
    }
}
