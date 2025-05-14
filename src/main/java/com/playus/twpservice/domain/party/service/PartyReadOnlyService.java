package com.playus.twpservice.domain.party.service;

import com.playus.twpservice.domain.party.dto.partybymatch.PartiesByMatchResponse;
import com.playus.twpservice.domain.party.feign.client.MatchFeignClient;
import com.playus.twpservice.domain.party.feign.client.UserFeignClient;
import com.playus.twpservice.domain.party.feign.response.PartyUserThumbnailUrlListResponse;
import com.playus.twpservice.domain.party.feign.response.PartyWriterInfoFeignResponse;
import com.playus.twpservice.domain.party.repository.read.PartyReadOnlyRepository;
import com.playus.twpservice.domain.party.vo.PartySummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartyReadOnlyService {

    private final PartyReadOnlyRepository partyRepository;
    private final UserFeignClient userFeignClient;
    private final MatchFeignClient matchFeignClient;

    public List<PartiesByMatchResponse> getPartiesBy(Long matchId) {

        List<PartySummary> partySummaries = partyRepository.findPartySummariesByMatchId(matchId);

        updateUserThumbnailUrls(partySummaries);
        updateWriterInfo(partySummaries);
        updateMatchDate(partySummaries, matchId);

        return partySummaries.stream()
                .map(PartySummary::toResponse)
                .toList();
    }

    private void updateUserThumbnailUrls(List<PartySummary> summaries) {
        summaries.forEach(party -> {
            List<Long> userIds = party.getUserIdList();
            PartyUserThumbnailUrlListResponse userThumbnails = userFeignClient.getPartyUserThumbnailUrls(userIds);
            party.updateUserThumbnailUrls(userThumbnails.thumbnailUrls());
        });
    }

    private void updateWriterInfo(List<PartySummary> summaries) {
        List<Long> writerIds = summaries.stream()
                .map(PartySummary::getWriterId)
                .toList();

        List<PartyWriterInfoFeignResponse> writerInfoList = userFeignClient.getWriterInfo(writerIds);

        IntStream.range(0, summaries.size()).forEach(i ->
                summaries.get(i).updateWriterInfo(writerInfoList.get(i)));
    }

    private void updateMatchDate(List<PartySummary> summaries, Long matchId) {
        LocalDateTime matchDate = matchFeignClient.getMatchDate(matchId);
        summaries.forEach(party -> party.updateMatchDate(matchDate));
    }
}
