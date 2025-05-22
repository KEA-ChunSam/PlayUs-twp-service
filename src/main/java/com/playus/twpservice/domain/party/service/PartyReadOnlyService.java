package com.playus.twpservice.domain.party.service;

import com.playus.twpservice.domain.common.security.CustomOAuth2User;
import com.playus.twpservice.domain.party.assertion.PartyAssert;
import com.playus.twpservice.domain.party.document.PartyDocument;
import com.playus.twpservice.domain.party.document.PartyJoinDocument;
import com.playus.twpservice.domain.party.dto.appliedparty.AppliedPartyResponse;
import com.playus.twpservice.domain.party.dto.applieduser.PartyAppliedUserResponse;
import com.playus.twpservice.domain.party.dto.detail.PartyDetailResponse;
import com.playus.twpservice.domain.party.dto.info.PartyInfoResponse;
import com.playus.twpservice.domain.party.enums.PartyAgeGroup;
import com.playus.twpservice.domain.party.enums.PartyJoinRequestStatus;
import com.playus.twpservice.domain.party.exception.document.PartyDocumentException;
import com.playus.twpservice.domain.party.feign.client.MatchFeignClient;
import com.playus.twpservice.domain.party.feign.client.UserFeignClient;
import com.playus.twpservice.domain.party.feign.response.PartyParticipantsInfoFeignResponse;
import com.playus.twpservice.domain.party.feign.response.PartyUserThumbnailUrlListResponse;
import com.playus.twpservice.domain.party.feign.response.PartyWriterInfoFeignResponse;
import com.playus.twpservice.domain.party.repository.read.PartyJoinReadOnlyRepository;
import com.playus.twpservice.domain.party.repository.read.PartyReadOnlyRepository;
import com.playus.twpservice.domain.party.vo.PartyInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartyReadOnlyService {

    private final PartyReadOnlyRepository partyRepository;
    private final UserFeignClient userFeignClient;
    private final MatchFeignClient matchFeignClient;
    private final PartyJoinReadOnlyRepository partyJoinReadOnlyRepository;


    // update method 는 msa 관련
    public List<PartyInfoResponse> getPartyInfoListByMatchId(Long matchId) {

        List<PartyInfo> partyInfoList = partyRepository.findPartyInfoList(matchId);

//        updateUserThumbnailUrls(partyInfoList);
//        updateWriterInfo(partyInfoList);
//        updateMatchDate(partyInfoList, matchId);

        return partyInfoList.stream()
                .map(PartyInfo::toResponse)
                .toList();
    }

    // update method 는 msa 관련
    public PartyDetailResponse getPartyDetail(Long partyId) {
        PartyInfo partyDetail = partyRepository.findPartyDetail(partyId)
                .orElseThrow(() -> new PartyDocumentException.NotFoundException("직관팟이 존재하지 않습니다!"));

//        updateUserThumbnailUrls(List.of(partyDetail));
//        updateWriterInfo(List.of(partyDetail));
//        updateMatchDate(List.of(partyDetail), partyDetail.getMatchId());

        return partyDetail.toPartyDetailResponse();
    }

    // update method 는 msa 관련
    public List<AppliedPartyResponse> getAppliedParties(CustomOAuth2User principal) {

        List<PartyInfo> appliedParties = partyRepository.findAppliedParties(principal.getId());
        updateWriterInfoWhenFindingAppliedParty(appliedParties);
        return appliedParties.stream()
                .map(PartyInfo::toAppliedPartyResponse)
                .toList();
    }

    public List<PartyAppliedUserResponse> getAppliedUsers(Long userId, Long partyId) {
        PartyDocument partyDocument = partyRepository.findById(partyId)
                .orElseThrow(() -> new PartyDocumentException.NotFoundException("직관팟이 존재하지 않습니다!"));

        PartyAssert.isLoginUserWriter(userId, partyDocument.getWriterId(), "방장이 아니면 직관팟 지원자를 조회할 수 없습니다!");

        List<PartyJoinDocument> waitingApplicants = partyJoinReadOnlyRepository.findByPartyIdAndStatus(partyDocument.getId(), PartyJoinRequestStatus.WAIT);

        if (waitingApplicants.isEmpty()) {
            return List.of();
        }

        // 지원자 ID 목록 추출
        List<Long> orderedUserIds = waitingApplicants.stream()
                .map(PartyJoinDocument::getUserId)
                .toList();

        // 사용자 ID를 키로 하는 요구 메시지 맵 생성
        Map<Long, String> userRequireMessageMap = waitingApplicants.stream()
                .collect(Collectors.toMap(
                        PartyJoinDocument::getUserId,
                        PartyJoinDocument::getRequireMessage
                ));

        // 지원자 정보 조회
        List<PartyParticipantsInfoFeignResponse> orderedUserInfos = userFeignClient.getPartyApplicantsInfo(orderedUserIds);

        // 주의: Feign 클라이언트가 요청 순서를 보존한다고 가정합니다
        return returnPartyApplicantInfoList(orderedUserIds, orderedUserInfos, userRequireMessageMap);
    }

    private static List<PartyAppliedUserResponse> returnPartyApplicantInfoList(
            List<Long> orderedUserIds,
            List<PartyParticipantsInfoFeignResponse> orderedUserInfos,
            Map<Long, String> userRequireMessageMap) {

        if (orderedUserIds.isEmpty()) {
            return List.of();
        }

        if (orderedUserIds.size() != orderedUserInfos.size()) {
            throw new IllegalStateException("지원자 정보 개수가 일치하지 않습니다.");
        }

        // orderedUserInfos[i]가 orderedUserIds[i]에 해당한다고 가정합니다
        return IntStream.range(0, orderedUserIds.size())
                .mapToObj(i -> {
                    Long userId = orderedUserIds.get(i);
                    PartyParticipantsInfoFeignResponse userInfo = orderedUserInfos.get(i);
                    String requireMessage = userRequireMessageMap.get(userId);

                    String ageGroupDescription = PartyAgeGroup.getAgeDescriptionByAge((userInfo.age() / 10) * 10);

                    return PartyAppliedUserResponse.of(
                            userId,
                            userInfo.name(),
                            ageGroupDescription,
                            userInfo.thumbnailUrl(),
                            requireMessage
                    );
                })
                .toList();
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

    private void updateWriterInfoWhenFindingAppliedParty(List<PartyInfo> summaries) {
        List<Long> writerIds = summaries.stream()
                .map(PartyInfo::getWriterId)
                .toList();

        List<PartyWriterInfoFeignResponse> writerInfoList = userFeignClient.getWriterInfo(writerIds);

        IntStream.range(0, summaries.size()).forEach(i ->
                summaries.get(i).updateWriterInfoWhenFindingAppliedParty(writerInfoList.get(i)));
    }

    private void updateMatchDate(List<PartyInfo> summaries, Long matchId) {
        LocalDateTime matchDate = matchFeignClient.getMatchDate(matchId);
        summaries.forEach(party -> party.updateMatchDate(matchDate));
    }
}
