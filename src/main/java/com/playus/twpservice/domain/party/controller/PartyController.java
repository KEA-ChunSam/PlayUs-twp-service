package com.playus.twpservice.domain.party.controller;

import com.playus.twpservice.domain.party.dto.partydescription.PartyDetailRequest;
import com.playus.twpservice.domain.party.dto.partydescription.PartyDetailResponse;
import com.playus.twpservice.domain.party.dto.partyinfobymatch.PartyInfoListByMatchRequest;
import com.playus.twpservice.domain.party.dto.partyinfobymatch.PartyInfoResponse;
import com.playus.twpservice.domain.party.dto.partycreate.PartyCreateRequest;
import com.playus.twpservice.domain.party.dto.partycreate.PartyCreateResponse;
import com.playus.twpservice.domain.party.dto.partyupdate.PartyIdRequest;
import com.playus.twpservice.domain.party.dto.partyupdate.PartyUpdateRequest;
import com.playus.twpservice.domain.party.dto.partyupdate.PartyUpdateResponse;
import com.playus.twpservice.domain.party.dto.presigned.PresignedUrlForSaveImageRequest;
import com.playus.twpservice.domain.party.dto.presigned.PresignedUrlForSaveImageResponse;
import com.playus.twpservice.domain.party.service.PartyReadOnlyService;
import com.playus.twpservice.domain.party.service.PartyService;
import com.playus.twpservice.domain.party.specification.PartyControllerSpecification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/party")
@RequiredArgsConstructor
public class PartyController implements PartyControllerSpecification {

    private final PartyService partyService;
    private final PartyReadOnlyService partyReadOnlyService;

    @PostMapping
    public ResponseEntity<PartyCreateResponse> createParty(@AuthenticationPrincipal Long userId, @Valid @RequestBody PartyCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(partyService.createParty(userId, request));
    }

    @GetMapping
    public List<PartyInfoResponse> getPartiesByMatchId(@Valid PartyInfoListByMatchRequest request) {
        return partyReadOnlyService.getPartyInfoListByMatchId(request.matchId());
    }

    @GetMapping("/{partyId}")
    public PartyDetailResponse getPartyDetail(@Valid PartyDetailRequest request) {
        return partyReadOnlyService.getPartyDetail(request.partyId());
    }

    @PutMapping("/{partyId}")
    public PartyUpdateResponse updateParty(@AuthenticationPrincipal Long userId, @Valid PartyIdRequest idRequest,
                                           @Valid @RequestBody PartyUpdateRequest request) {
        return partyService.updateParty(userId, request.toParty(idRequest.partyId()));
    }

    @PostMapping("/presigned-url")
    public PresignedUrlForSaveImageResponse generatePresignedUrlForSaveImage(@Valid @RequestBody PresignedUrlForSaveImageRequest request) {
        return partyService.generatePresignedUrlForSaveImage(request);
    }
}
