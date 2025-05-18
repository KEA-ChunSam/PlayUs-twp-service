package com.playus.twpservice.domain.party.controller;

import com.playus.twpservice.domain.party.dto.delete.PartyDeleteResponse;
import com.playus.twpservice.domain.party.dto.detail.PartyDetailRequest;
import com.playus.twpservice.domain.party.dto.detail.PartyDetailResponse;
import com.playus.twpservice.domain.party.dto.info.PartyInfoRequest;
import com.playus.twpservice.domain.party.dto.info.PartyInfoResponse;
import com.playus.twpservice.domain.party.dto.create.PartyCreateRequest;
import com.playus.twpservice.domain.party.dto.create.PartyCreateResponse;
import com.playus.twpservice.domain.common.request.PartyIdRequest;
import com.playus.twpservice.domain.party.dto.update.PartyUpdateRequest;
import com.playus.twpservice.domain.party.dto.update.PartyUpdateResponse;
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
    public List<PartyInfoResponse> getPartiesByMatchId(@Valid PartyInfoRequest request) {
        return partyReadOnlyService.getPartyInfoListByMatchId(request.matchId());
    }

    @GetMapping("/{partyId}")
    public PartyDetailResponse getPartyDetail(@Valid PartyDetailRequest request) {
        return partyReadOnlyService.getPartyDetail(request.partyId());
    }

    @PutMapping("/{partyId}")
    public PartyUpdateResponse updateParty(@AuthenticationPrincipal Long userId, @Valid PartyIdRequest idRequest,
                                           @Valid @RequestBody PartyUpdateRequest request) {
        return partyService.updateParty(userId, idRequest, request);
    }

    @PatchMapping("/{partyId}")
    public PartyDeleteResponse deleteParty(@AuthenticationPrincipal Long userId, @Valid PartyIdRequest idRequest) {
        return partyService.deleteParty(userId, idRequest.partyId());
    }

    @PostMapping("/{partyId}/apply/fcfs")
    public PartyJoinResponse applyPartyFCFS(@AuthenticationPrincipal Long userId, @Valid PartyIdRequest idRequest) {
        return null;
    }

    @PostMapping("/presigned-url")
    public PresignedUrlForSaveImageResponse generatePresignedUrlForSaveImage(@Valid @RequestBody PresignedUrlForSaveImageRequest request) {
        return partyService.generatePresignedUrlForSaveImage(request);
    }
}
