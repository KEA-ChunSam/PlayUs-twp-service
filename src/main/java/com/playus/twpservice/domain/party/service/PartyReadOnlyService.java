package com.playus.twpservice.domain.party.service;

import com.playus.twpservice.domain.common.security.CustomOAuth2User;
import com.playus.twpservice.domain.party.assertion.PartyAssert;
import com.playus.twpservice.domain.party.document.PartyDocument;
import com.playus.twpservice.domain.party.document.PartyJoinDocument;
import com.playus.twpservice.domain.party.dto.appliedparty.AppliedPartyResponse;
import com.playus.twpservice.domain.party.dto.applieduser.PartyAppliedUserResponse;
import com.playus.twpservice.domain.party.dto.detail.PartyDetailResponse;
import com.playus.twpservice.domain.party.dto.info.PartyInfoResponse;
import com.playus.twpservice.domain.party.dto.participants.PartyParticipantsInfoResponse;
import com.playus.twpservice.domain.party.enums.PartyAgeGroup;
import com.playus.twpservice.domain.party.enums.PartyJoinRequestStatus;
import com.playus.twpservice.domain.party.exception.document.PartyDocumentException;
import com.playus.twpservice.domain.common.feign.client.UserFeignClient;
import com.playus.twpservice.domain.common.feign.response.PartyParticipantsInfoFeignResponse;
import com.playus.twpservice.domain.common.feign.response.PartyUserThumbnailUrlListResponse;
import com.playus.twpservice.domain.common.feign.response.PartyWriterInfoFeignResponse;
import com.playus.twpservice.domain.party.exception.entity.PartyException;
import com.playus.twpservice.domain.party.repository.read.PartyJoinReadOnlyRepository;
import com.playus.twpservice.domain.party.repository.read.PartyReadOnlyRepository;
import com.playus.twpservice.domain.party.vo.PartyInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final PartyJoinReadOnlyRepository partyJoinReadOnlyRepository;


    // update method 는 msa 관련
    public List<PartyInfoResponse> getPartyInfoListByMatchId(Long matchId) {

        List<PartyInfo> partyInfoList = partyRepository.findPartyInfoList(matchId);

        updateUserThumbnailUrls(partyInfoList);
        updateWriterInfo(partyInfoList);

        return partyInfoList.stream()
                .map(PartyInfo::toResponse)
                .toList();
    }

    // update method 는 msa 관련
    public PartyDetailResponse getPartyDetail(Long partyId) {
        PartyInfo partyDetail = partyRepository.findPartyDetail(partyId)
                .orElseThrow(() -> new PartyDocumentException.NotFoundException("직관팟이 존재하지 않습니다!"));

        updateUserThumbnailUrls(List.of(partyDetail));
        updateWriterInfo(List.of(partyDetail));

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

    public List<PartyParticipantsInfoResponse> getParticipants(CustomOAuth2User principal, Long partyId) {

        Long userId = principal.getId();

        PartyDocument partyDocument = partyRepository.findById(partyId)
                .orElseThrow(() -> new PartyDocumentException.NotFoundException("직관팟이 존재하지 않습니다!"));

        List<Long> participantIds = getParticipantsIdWithoutLoginUser(partyDocument, userId);

        List<PartyParticipantsInfoFeignResponse> partyParticipantsInfoFeignResponses = userFeignClient.getPartyApplicantsInfo(participantIds);

        return getParticipantsInfoList(participantIds, partyParticipantsInfoFeignResponses);
    }

    private List<PartyParticipantsInfoResponse> getParticipantsInfoList(List<Long> participantIds, List<PartyParticipantsInfoFeignResponse> partyParticipantsInfoFeignResponses) {
        // Feign 응답을 userId 기준으로 Map으로 변환
        Map<Long, PartyParticipantsInfoFeignResponse> userInfoMap = partyParticipantsInfoFeignResponses.stream()
                .collect(Collectors.toMap(
                        PartyParticipantsInfoFeignResponse::userId,
                        response -> response
                ));

        // participantIds를 순회하면서 해당하는 userInfo 매핑
        return participantIds.stream()
                .map(participantId -> {
                    PartyParticipantsInfoFeignResponse userInfo = userInfoMap.get(participantId);
                    if (userInfo == null) {
                        throw new IllegalStateException("참가자 ID " + participantId + "에 대한 사용자 정보를 찾을 수 없습니다.");
                    }

                    return PartyParticipantsInfoResponse.of(
                            participantId,
                            userInfo.name()
                    );
                })
                .toList();
    }


    private List<Long> getParticipantsIdWithoutLoginUser(PartyDocument partyDocument, Long userId) {
        List<Long> participantIds = partyJoinReadOnlyRepository.findByPartyIdAndStatus(partyDocument.getId(), PartyJoinRequestStatus.ACCEPT).stream()
                .map(PartyJoinDocument::getUserId)
                .collect(Collectors.toList());

        participantIds.add(partyDocument.getWriterId());
        if (!participantIds.contains(userId)) {
            throw new PartyException.ParticipantsNotFoundException("이 직관팟에 참여하지 않은 인원입니다!");
        }
        participantIds.remove(userId);
        return participantIds;
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
                summaries.get(i).updateWriterInfoWhenUpdatingWriterThumbnailOnly(writerInfoList.get(i)));
    }
}
