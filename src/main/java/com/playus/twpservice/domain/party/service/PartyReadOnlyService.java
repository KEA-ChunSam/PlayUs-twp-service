package com.playus.twpservice.domain.party.service;

import com.playus.twpservice.domain.party.dto.partybymatch.PartiesByMatchResponse;
import com.playus.twpservice.domain.party.repository.read.PartyAgeReadOnlyRepository;
import com.playus.twpservice.domain.party.repository.read.PartyJoinReadOnlyRepository;
import com.playus.twpservice.domain.party.repository.read.PartyReadOnlyRepository;
import com.playus.twpservice.domain.party.repository.read.PartyThumbnailUrlReadOnlyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartyReadOnlyService {

    private final PartyReadOnlyRepository partyRepository;
    private final PartyAgeReadOnlyRepository partyAgeRepository;
    private final PartyJoinReadOnlyRepository partyJoinRepository;
    private final PartyThumbnailUrlReadOnlyRepository partyThumbnailUrlRepository;

    public PartiesByMatchResponse getPartiesBy(Long matchId, Long userId) {

        // 1. 직관팟 데이터 가져오기 (title, method, ages, gender, currentParticipantsCount,
        //                         maximumParticipantsCount, thumbnailurls)



        // 2. user 데이터 가져오기 (authorName, authorGender, userThumbnailUrls)

        // 3. match 데이터 가져오기 (matchDate)

        return null;
    }
}
