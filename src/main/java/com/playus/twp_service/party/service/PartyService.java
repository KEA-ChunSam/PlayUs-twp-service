package com.playus.twp_service.party.service;

import com.playus.twp_service.party.dto.party_create.PartyCreateRequest;
import com.playus.twp_service.party.dto.party_create.PartyCreateResponse;
import com.playus.twp_service.party.entity.Party;
import com.playus.twp_service.party.entity.PartyAge;
import com.playus.twp_service.party.entity.PartyJoin;
import com.playus.twp_service.party.enums.PartyAgeGroup;
import com.playus.twp_service.party.enums.Status;
import com.playus.twp_service.party.repository.write.PartyAgeRepository;
import com.playus.twp_service.party.repository.write.PartyJoinRepository;
import com.playus.twp_service.party.repository.write.PartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PartyService {


    private final PartyRepository partyRepository;
    private final PartyJoinRepository partyJoinRepository;
    private final PartyAgeRepository partyAgeRepository;


    public PartyCreateResponse createParty(Long userId, PartyCreateRequest request) {

        Party party = partyRepository.save(request.toParty());

        partyJoinRepository.save(PartyJoin.create(userId, party, Status.ACCEPT, null));

        List<PartyAge> partyAgeList = convertStringPartyAgeToIntegerPartyAge(request, party);
        partyAgeRepository.saveAll(partyAgeList);

        return PartyCreateResponse.of(party.getId(), Boolean.TRUE);
    }

    private static List<PartyAge> convertStringPartyAgeToIntegerPartyAge(PartyCreateRequest request, Party party) {
        return request.ageGroup().stream()
                .map(age -> PartyAge.create(party, PartyAgeGroup.getAgeByDescription(age)))
                .toList();
    }
}
