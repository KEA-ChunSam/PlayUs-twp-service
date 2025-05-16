package com.playus.twpservice.domain.party.service;

import com.playus.twpservice.domain.chat.entity.ChatPart;
import com.playus.twpservice.domain.chat.entity.ChatRoom;
import com.playus.twpservice.domain.chat.repository.ChatMessageRepository;
import com.playus.twpservice.domain.chat.repository.ChatPartRepository;
import com.playus.twpservice.domain.chat.repository.ChatRoomRepository;
import com.playus.twpservice.domain.party.assertion.PartyAssert;
import com.playus.twpservice.domain.party.document.PartyDocument;
import com.playus.twpservice.domain.party.dto.partycreate.PartyCreateRequest;
import com.playus.twpservice.domain.party.dto.partycreate.PartyCreateResponse;
import com.playus.twpservice.domain.party.dto.partydelete.PartyDeleteResponse;
import com.playus.twpservice.domain.common.PartyIdRequest;
import com.playus.twpservice.domain.party.dto.partyupdate.PartyUpdateRequest;
import com.playus.twpservice.domain.party.dto.partyupdate.PartyUpdateResponse;
import com.playus.twpservice.domain.party.dto.presigned.PresignedUrlForSaveImageRequest;
import com.playus.twpservice.domain.party.dto.presigned.PresignedUrlForSaveImageResponse;
import com.playus.twpservice.domain.party.entity.Party;
import com.playus.twpservice.domain.party.entity.PartyAge;
import com.playus.twpservice.domain.party.entity.PartyThumbnailUrl;
import com.playus.twpservice.domain.party.enums.PartyAgeGroup;
import com.playus.twpservice.domain.party.repository.read.PartyReadOnlyRepository;
import com.playus.twpservice.domain.party.repository.write.PartyAgeRepository;
import com.playus.twpservice.domain.party.repository.write.PartyJoinRepository;
import com.playus.twpservice.domain.party.repository.write.PartyRepository;
import com.playus.twpservice.domain.party.repository.write.PartyThumbnailUrlRepository;
import com.playus.twpservice.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.playus.twpservice.domain.party.exception.entity.PartyException.*;

@Service
@Transactional
@RequiredArgsConstructor
public class PartyService {

    private final PartyRepository partyRepository;
    private final PartyAgeRepository partyAgeRepository;
    private final PartyThumbnailUrlRepository partyThumbnailUrlRepository;


    private final ChatRoomRepository chatRoomRepository;
    private final ChatPartRepository chatPartRepository;
    private final ChatMessageRepository chatMessageRepository;

    private final S3Service s3Service;
    private final PartyReadOnlyRepository partyReadOnlyRepository;
    private final PartyJoinRepository partyJoinRepository;

    public PartyCreateResponse createParty(Long userId, PartyCreateRequest request) {
        ChatRoom chatRoom = initializeChatRoomAsWriter(userId, request);

        Party party = partyRepository.save(request.toPartyWith(userId).assignChatRoom(chatRoom.getId()));

        List<PartyAge> partyAgeList = toPartyAgeEntity(request, party);
        partyAgeRepository.saveAll(partyAgeList);

        saveThumbnailUrlIfPresent(request, party);

        return PartyCreateResponse.of(party.getId(), chatRoom.getId());
    }

    public PartyUpdateResponse updateParty(Long userId, PartyIdRequest idRequest, PartyUpdateRequest updateRequest) {
        Long partyId = idRequest.partyId();
        Long writerId = updateRequest.writerId();

        PartyAssert.isLoginUserWriter(userId, writerId);

        Party savedParty = partyRepository.findById(partyId)
                .orElseThrow(() -> new NotFoundException("잘못된 직관팟 번호입니다!"));

        savedParty.updateParty(updateRequest);

        updatePartyThumbnails(updateRequest, partyId, savedParty);
        updatePartyAgeGroup(updateRequest, partyId, savedParty);

        return PartyUpdateResponse.of(partyId);
    }

    public PartyDeleteResponse deleteParty(Long userId, Long partyId, Long writerId) {

        PartyAssert.isLoginUserWriter(userId, writerId);

        PartyDocument partyDocument = partyReadOnlyRepository.findById(partyId)
                .orElseThrow(() -> new NotFoundException("잘못된 직관팟 번호입니다!"));

        partyThumbnailUrlRepository.deleteByPartyId(partyId);
        partyAgeRepository.deleteByPartyId(partyId);
        partyJoinRepository.deleteByPartyId(partyId);
        partyRepository.deleteById(partyId);

        String chatRoomId = partyDocument.getChatRoomId();

        List<ChatPart> chatParts = chatPartRepository.findByChatRoomId(chatRoomId);
        List<String> chatPartIds = chatParts.stream()
                .map(ChatPart::getId)
                .toList();


        chatMessageRepository.deleteAllByChatPartIds(chatPartIds);
        chatPartRepository.deleteByChatRoomId(chatRoomId);
        chatRoomRepository.deleteById(chatRoomId);

        return PartyDeleteResponse.of(partyId);
    }

    public PresignedUrlForSaveImageResponse generatePresignedUrlForSaveImage(PresignedUrlForSaveImageRequest request) {
        return new PresignedUrlForSaveImageResponse(s3Service.generatePresignedUrl(request.imageFileName()));
    }



    private void updatePartyAgeGroup(PartyUpdateRequest updateRequest, Long partyId, Party savedParty) {
        partyAgeRepository.deleteByPartyId(partyId);
        partyAgeRepository.saveAll(
                updateRequest.ageGroup().stream()
                        .map(age -> PartyAge.create(savedParty, PartyAgeGroup.getAgeByDescription(age)))
                        .toList()
        );
    }

    private void updatePartyThumbnails(PartyUpdateRequest updateRequest, Long partyId, Party savedParty) {
        partyThumbnailUrlRepository.deleteByPartyId(partyId);
        partyThumbnailUrlRepository.saveAll(
                updateRequest.thumbnailUrl().stream()
                        .map(thumbnailUrl -> PartyThumbnailUrl.create(savedParty, thumbnailUrl))
                        .toList()
        );
    }

    private ChatRoom initializeChatRoomAsWriter(Long userId, PartyCreateRequest request) {
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create(request.title()));
        chatPartRepository.save(ChatPart.create(userId, chatRoom.getId()));
        return chatRoom;
    }

    private void saveThumbnailUrlIfPresent(PartyCreateRequest request, Party party) {
        if (thumbnailUrlExistsIn(request)) {
            List<PartyThumbnailUrl> partyThumbnailUrlList = toPartyThumbnailUrlEntity(request, party);
            partyThumbnailUrlRepository.saveAll(partyThumbnailUrlList);
        }
    }

    private static List<PartyThumbnailUrl> toPartyThumbnailUrlEntity(PartyCreateRequest request, Party party) {
        return request.thumbnailUrl().stream()
                .map(thumbnailUrl -> PartyThumbnailUrl.create(party, thumbnailUrl))
                .toList();
    }

    private static boolean thumbnailUrlExistsIn(PartyCreateRequest request) {
        return !Objects.isNull(request.thumbnailUrl()) && !request.thumbnailUrl().isEmpty();
    }

    private static List<PartyAge> toPartyAgeEntity(PartyCreateRequest request, Party party) {
        return request.ageGroup().stream()
                .map(age -> PartyAge.create(party, PartyAgeGroup.getAgeByDescription(age)))
                .toList();
    }
}
