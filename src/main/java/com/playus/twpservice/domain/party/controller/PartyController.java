package com.playus.twpservice.domain.party.controller;

import com.playus.twpservice.domain.party.dto.party_create.PartyCreateRequest;
import com.playus.twpservice.domain.party.dto.party_create.PartyCreateResponse;
import com.playus.twpservice.domain.party.dto.presigned.PresignedUrlForSaveImageRequest;
import com.playus.twpservice.domain.party.dto.presigned.PresignedUrlForSaveImageResponse;
import com.playus.twpservice.domain.party.service.PartyService;
import com.playus.twpservice.domain.party.specification.PartyControllerSpecification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/party")
@RequiredArgsConstructor
public class PartyController implements PartyControllerSpecification {

    private final PartyService partyService;

    @PostMapping
    public ResponseEntity<PartyCreateResponse> createParty(@AuthenticationPrincipal Long userId, @Valid @RequestBody PartyCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(partyService.createParty(userId, request));
    }

    @PostMapping("/presigned-url")
    public PresignedUrlForSaveImageResponse generatePresignedUrlForSaveImage(@Valid @RequestBody PresignedUrlForSaveImageRequest request) {
        return partyService.generatePresignedUrlForSaveImage(request);
    }
}
