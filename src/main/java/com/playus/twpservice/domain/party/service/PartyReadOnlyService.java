package com.playus.twpservice.domain.party.service;

import com.playus.twpservice.domain.party.dto.partybymatch.PartiesByMatchResponse;
import com.playus.twpservice.domain.party.feign.client.MatchFeignClient;
import com.playus.twpservice.domain.party.feign.client.UserFeignClient;
import com.playus.twpservice.domain.party.feign.response.PartyWriterInfoFeignResponse;
import com.playus.twpservice.domain.party.repository.read.PartyReadOnlyRepository;
import com.playus.twpservice.domain.party.vo.PartySummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartyReadOnlyService {

    private final PartyReadOnlyRepository partyRepository;
    private final UserFeignClient userFeignClient;
    private final MatchFeignClient matchFeignClient;

    public List<PartiesByMatchResponse> getPartiesBy(Long matchId, Long userId) {

        // 1. 직관팟 데이터 가져오기 (title, method, ages, gender, currentParticipantsCount,
        //                         maximumParticipantsCount, thumbnailurls)

        List<PartySummary> partySummaries = partyRepository.findPartySummariesByMatchId(matchId);

        // 2. 각 party 대한 user 데이터 가져오기 (thumbnailUrls)
        for(int j=0;j< partySummaries.size();j++) {
            PartySummary partySummary = partySummaries.get(j);
            List<String> userThumbnailUrls = userFeignClient.getPartyUserThumbnailUrls(partySummary.getUserIdList());
            partySummary.updateUserThumbnailUrls(userThumbnailUrls);
        }

        // 3. author 데이터 가져오기 (writerName, writerGender, writerThumbnailUrl)
        List<Long> writerIdList = partySummaries.stream()
                .map(PartySummary::getWriterId)
                .toList();

        List<PartyWriterInfoFeignResponse> writerInfoList = userFeignClient.getWriterInfo(writerIdList);
        for(int i=0;i < partySummaries.size();i++) {
            partySummaries.get(i).updateWriterInfo(writerInfoList.get(i));
        }

        // 4. match 데이터 가져오기 (matchDate)
        List<Long> matchIdList = partySummaries.stream()
                .map(PartySummary::getMatchId)
                .toList();
        List<LocalDateTime> matchDateList = matchFeignClient.getMatchDate(matchIdList);
        for(int i=0;i<partySummaries.size();i++) {
            partySummaries.get(i).updateMatchDate(matchDateList.get(i));
        }

        // 5. 1, 2, 3, 4 정보 기반으로 response 생성
        List<PartiesByMatchResponse> results = new ArrayList();
        for (PartySummary partySummary : partySummaries) {
            results.add(partySummary.toResponse());
        }

        return results;
    }
}
