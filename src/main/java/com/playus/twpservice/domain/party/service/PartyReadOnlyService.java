package com.playus.twpservice.domain.party.service;

import com.playus.twpservice.domain.party.assertion.PartyAssert;
import com.playus.twpservice.domain.party.document.PartyDocument;
import com.playus.twpservice.domain.party.document.PartyJoinDocument;
import com.playus.twpservice.domain.party.dto.applieduser.PartyAppliedUserResponse;
import com.playus.twpservice.domain.party.dto.detail.PartyDetailResponse;
import com.playus.twpservice.domain.party.dto.info.PartyInfoResponse;
import com.playus.twpservice.domain.party.enums.PartyAgeGroup;
import com.playus.twpservice.domain.party.enums.PartyJoinRequestStatus;
import com.playus.twpservice.domain.party.exception.document.PartyDocumentException;
import com.playus.twpservice.domain.party.feign.client.MatchFeignClient;
import com.playus.twpservice.domain.party.feign.client.UserFeignClient;
import com.playus.twpservice.domain.party.feign.response.PartyApplicantsInfoFeignResponse;
import com.playus.twpservice.domain.party.feign.response.PartyUserThumbnailUrlListResponse;
import com.playus.twpservice.domain.party.feign.response.PartyWriterInfoFeignResponse;
import com.playus.twpservice.domain.party.repository.read.PartyJoinReadOnlyRepository;
import com.playus.twpservice.domain.party.repository.read.PartyReadOnlyRepository;
import com.playus.twpservice.domain.party.vo.PartyInfo;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartyReadOnlyService {

    private final PartyReadOnlyRepository partyRepository;
    private final UserFeignClient userFeignClient;
    private final MatchFeignClient matchFeignClient;
    private final PartyJoinReadOnlyRepository partyJoinReadOnlyRepository;

    public List<PartyInfoResponse> getPartyInfoListByMatchId(Long matchId) {

        List<PartyInfo> partyInfoList = partyRepository.findPartyInfoList(matchId);

        updateUserThumbnailUrls(partyInfoList);
        updateWriterInfo(partyInfoList);
        updateMatchDate(partyInfoList, matchId);

        return partyInfoList.stream()
                .map(PartyInfo::toResponse)
                .toList();
    }

    public PartyDetailResponse getPartyDetail(Long partyId) {
        PartyInfo partyDetail = partyRepository.findPartyDetail(partyId)
                .orElseThrow(() -> new PartyDocumentException.NotFoundException("직관팟이 존재하지 않습니다!"));

        updateUserThumbnailUrls(List.of(partyDetail));
        updateWriterInfo(List.of(partyDetail));
        updateMatchDate(List.of(partyDetail), partyDetail.getMatchId());

        return partyDetail.toPartyDetailResponse();
    }

    public List<PartyAppliedUserResponse> getAppliedUsers(Long userId, Long partyId) {
        PartyDocument partyDocument = partyRepository.findById(partyId)
                .orElseThrow(() -> new PartyDocumentException.NotFoundException("직관팟이 존재하지 않습니다!"));

        PartyAssert.isLoginUserWriter(userId, partyDocument.getWriterId(), "방장이 아니면 직관팟 지원자를 조회할 수 없습니다!");

        List<PartyJoinDocument> waitingApplicants = partyJoinReadOnlyRepository.findByPartyIdAndStatus(partyDocument.getId(), PartyJoinRequestStatus.WAIT);

        List<Long> applicantsIdList = waitingApplicants
                .stream()
                .map(PartyJoinDocument::getUserId)
                .toList();

        List<PartyApplicantsInfoFeignResponse> partyApplicantsInfo = userFeignClient.getPartyApplicantsInfo(applicantsIdList);

        List<String> requireMessageList = waitingApplicants
                .stream()
                .map(PartyJoinDocument::getRequireMessage)
                .toList();

        List<PartyAppliedUserResponse> responseList = new ArrayList<>();

        for (int i = 0; i < applicantsIdList.size(); i++) {
            Long applicantId = applicantsIdList.get(i);
            PartyApplicantsInfoFeignResponse info = partyApplicantsInfo.get(i);
            String requireMessage = requireMessageList.get(i);

            String ageGroupDescription = PartyAgeGroup.getAgeDescriptionByAge((info.age()/10) * 10);

            PartyAppliedUserResponse response = PartyAppliedUserResponse.of(
                    applicantId,
                    info.name(),
                    ageGroupDescription,
                    info.thumbnailUrl(),
                    requireMessage
            );

            responseList.add(response);
        }

        return responseList;
    }

    private void updateUserThumbnailUrls(List<PartyInfo> summaries) {
        summaries.forEach(party -> {
            List<Long> userIds = party.getUserIdList();
            PartyUserThumbnailUrlListResponse userThumbnails = userFeignClient.getPartyUserThumbnailUrls(userIds);
            party.updateUserThumbnailUrls(userThumbnails.thumbnailUrls());
        });
    }

    private void updateWriterInfo(List<PartyInfo> summaries) {
        List<Long> writerIds = summaries.stream()
                .map(PartyInfo::getWriterId)
                .toList();

        List<PartyWriterInfoFeignResponse> writerInfoList = userFeignClient.getWriterInfo(writerIds);

        IntStream.range(0, summaries.size()).forEach(i ->
                summaries.get(i).updateWriterInfo(writerInfoList.get(i)));
    }

    private void updateMatchDate(List<PartyInfo> summaries, Long matchId) {
        LocalDateTime matchDate = matchFeignClient.getMatchDate(matchId);
        summaries.forEach(party -> party.updateMatchDate(matchDate));
    }
}
