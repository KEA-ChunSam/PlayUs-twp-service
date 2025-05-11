package com.playus.twpservice.domain.party.service;

import com.playus.twpservice.domain.chat.entity.ChatPart;
import com.playus.twpservice.domain.chat.entity.ChatRoom;
import com.playus.twpservice.domain.chat.repository.ChatPartRepository;
import com.playus.twpservice.domain.chat.repository.ChatRoomRepository;
import com.playus.twpservice.domain.party.dto.party_create.PartyCreateRequest;
import com.playus.twpservice.domain.party.dto.party_create.PartyCreateResponse;
import com.playus.twpservice.domain.party.dto.presigned.PresignedUrlForSaveImageRequest;
import com.playus.twpservice.domain.party.dto.presigned.PresignedUrlForSaveImageResponse;
import com.playus.twpservice.domain.party.entity.Party;
import com.playus.twpservice.domain.party.entity.PartyAge;
import com.playus.twpservice.domain.party.entity.PartyJoin;
import com.playus.twpservice.domain.party.entity.PartyThumbnailUrl;
import com.playus.twpservice.domain.party.enums.PartyAgeGroup;
import com.playus.twpservice.domain.party.enums.PartyJoinRequestStatus;
import com.playus.twpservice.domain.party.repository.write.PartyAgeRepository;
import com.playus.twpservice.domain.party.repository.write.PartyJoinRepository;
import com.playus.twpservice.domain.party.repository.write.PartyRepository;
import com.playus.twpservice.domain.party.repository.write.PartyThumbnailUrlRepository;
import com.playus.twpservice.global.s3.S3PresignedUrlGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class PartyService {

    private final PartyRepository partyRepository;
    private final PartyJoinRepository partyJoinRepository;
    private final PartyAgeRepository partyAgeRepository;
    private final PartyThumbnailUrlRepository partyThumbnailUrlRepository;

    private final ChatRoomRepository chatRoomRepository;
    private final ChatPartRepository chatPartRepository;

    private final S3PresignedUrlGenerator s3PresignedUrlGenerator;

    public PartyCreateResponse createParty(Long userId, PartyCreateRequest request) {
        ChatRoom chatRoom = initializeChatRoomAsWriter(userId, request);

        Party party = partyRepository.save(request.toParty().assignChatRoom(chatRoom.getId()));

        partyJoinRepository.save(PartyJoin.create(userId, party, PartyJoinRequestStatus.ACCEPT, null));

        List<PartyAge> partyAgeList = toPartyAgeEntity(request, party);
        partyAgeRepository.saveAll(partyAgeList);

        saveThumbnailUrlIfPresent(request, party);

        return PartyCreateResponse.of(party.getId(), chatRoom.getId());
    }

    public PresignedUrlForSaveImageResponse generatePresignedUrlForSaveImage(PresignedUrlForSaveImageRequest request) {
        return new PresignedUrlForSaveImageResponse(s3PresignedUrlGenerator.generatePresignedUrl(request.imageFileName()));
    }

    private ChatRoom initializeChatRoomAsWriter(Long userId, PartyCreateRequest request) {
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create(request.title()));
        chatPartRepository.save(ChatPart.create(userId, chatRoom));
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
