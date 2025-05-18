package com.playus.twpservice.domain.party.service;

import com.playus.twpservice.IntegrationTestSupport;
import com.playus.twpservice.domain.chat.entity.ChatMessage;
import com.playus.twpservice.domain.chat.entity.ChatPart;
import com.playus.twpservice.domain.chat.entity.ChatRoom;
import com.playus.twpservice.domain.chat.repository.ChatMessageRepository;
import com.playus.twpservice.domain.chat.repository.ChatPartRepository;
import com.playus.twpservice.domain.chat.repository.ChatRoomRepository;
import com.playus.twpservice.domain.party.document.PartyDocument;
import com.playus.twpservice.domain.party.dto.create.PartyCreateRequest;
import com.playus.twpservice.domain.party.dto.create.PartyCreateResponse;
import com.playus.twpservice.domain.common.request.PartyIdRequest;
import com.playus.twpservice.domain.party.dto.delete.PartyDeleteResponse;
import com.playus.twpservice.domain.party.dto.update.PartyUpdateRequest;
import com.playus.twpservice.domain.party.dto.update.PartyUpdateResponse;
import com.playus.twpservice.domain.party.dto.presigned.PresignedUrlForSaveImageRequest;
import com.playus.twpservice.domain.party.dto.presigned.PresignedUrlForSaveImageResponse;
import com.playus.twpservice.domain.party.entity.Party;
import com.playus.twpservice.domain.party.entity.PartyAge;
import com.playus.twpservice.domain.party.entity.PartyJoin;
import com.playus.twpservice.domain.party.entity.PartyThumbnailUrl;
import com.playus.twpservice.domain.party.enums.PartyGender;
import com.playus.twpservice.domain.party.enums.PartyJoinMethod;
import com.playus.twpservice.domain.party.enums.PartyJoinRequestStatus;
import com.playus.twpservice.domain.party.exception.entity.PartyException;
import com.playus.twpservice.domain.party.repository.read.PartyReadOnlyRepository;
import com.playus.twpservice.domain.party.repository.write.PartyAgeRepository;
import com.playus.twpservice.domain.party.repository.write.PartyJoinRepository;
import com.playus.twpservice.domain.party.repository.write.PartyRepository;
import com.playus.twpservice.domain.party.repository.write.PartyThumbnailUrlRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

class PartyServiceTest extends IntegrationTestSupport {

    @Autowired
    private PartyService partyService;

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private PartyJoinRepository partyJoinRepository;

    @Autowired
    private PartyAgeRepository partyAgeRepository;

    @Autowired
    private PartyThumbnailUrlRepository partyThumbnailUrlRepository;

    @Autowired
    private PartyReadOnlyRepository partyReadOnlyRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatPartRepository chatPartRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @AfterEach
    void tearDown() {
        partyThumbnailUrlRepository.deleteAll();
        partyAgeRepository.deleteAll();
        partyJoinRepository.deleteAll();
        partyRepository.deleteAll();
        partyReadOnlyRepository.deleteAll();

        chatRoomRepository.deleteAll();
        chatMessageRepository.deleteAll();
        chatPartRepository.deleteAll();
    }

    @DisplayName("직관팟을 생성할 수 있다.")
    @Test
    void createParty() {
        // given
        Long userId = 1L;
        PartyCreateRequest request = PartyCreateRequest.of("title", "선착순", "남자만", List.of("10대", "20대"),
                1L, 10L, List.of("url", "url2"), 1L, "message");

        // when
        PartyCreateResponse result = partyService.createParty(userId, request);

        // then
        assertThat(partyRepository.count()).isEqualTo(1);
        assertThat(partyJoinRepository.count()).isZero();
        assertThat(partyThumbnailUrlRepository.count()).isEqualTo(2);
        assertThat(chatRoomRepository.count()).isEqualTo(1);
        assertThat(chatPartRepository.count()).isEqualTo(1);

        ChatRoom savedChatRoom = chatRoomRepository.findAll().get(0);
        assertThat(savedChatRoom.getId()).isEqualTo(result.chatRoomId());
        assertThat(savedChatRoom.getRoomName()).isEqualTo("title");

        ChatPart savedChatPart = chatPartRepository.findAll().get(0);
        assertThat(savedChatPart.getUserId()).isEqualTo(userId);
        assertThat(savedChatPart.getChatRoomId()).isEqualTo(savedChatRoom.getId());

        Party savedParty = partyRepository.findAll().get(0);
        assertThat(savedParty.getTitle()).isEqualTo("title");
        assertThat(savedParty.getMinimumParticipants()).isEqualTo(1L);
        assertThat(savedParty.getMaximumParticipants()).isEqualTo(10L);
        assertThat(savedParty.getCurrentParticipants()).isEqualTo(1L);
        assertThat(savedParty.getPartyGender()).isEqualTo(PartyGender.MALE);
        assertThat(savedParty.getPartyJoinMethod()).isEqualTo(PartyJoinMethod.FIRST_COME);
        assertThat(savedParty.getWriterId()).isEqualTo(userId);
        assertThat(savedParty.getChatRoomId()).isEqualTo(savedChatRoom.getId());

        List<PartyThumbnailUrl> savedUrl = partyThumbnailUrlRepository.findAll();
        assertThat(savedUrl).hasSize(2);
        assertThat(savedUrl)
                .extracting("thumbnailUrl")
                .containsExactlyInAnyOrder("url", "url2");

        List<PartyAge> savedPartyAges = partyAgeRepository.findAll();
        assertThat(savedPartyAges).hasSize(2);
        assertThat(savedPartyAges)
                .extracting("age")
                .containsExactlyInAnyOrder(10, 20);

        Long savedPartyId = savedParty.getId();
        assertThat(savedUrl).allMatch(url -> url.getParty().getId().equals(savedPartyId));
        assertThat(savedPartyAges).allMatch(age -> age.getParty().getId().equals(savedPartyId));

        assertThat(result)
                .extracting("partyId", "chatRoomId")
                .containsExactly(savedPartyId, savedChatRoom.getId());
    }

    @DisplayName("썸네일 URL이 비어 있을 때에도 직관팟을 생성할 수 있다.")
    @Test
    void createParty_EMPTY_IMAGE_URL() {
        // given
        Long userId = 1L;
        PartyCreateRequest request = PartyCreateRequest.of("title", "선착순", "남자만",
                List.of("10대", "20대"), 1L, 10L, List.of(), 1L, "message");

        // when
        partyService.createParty(userId, request);

        // then
        assertThat(partyRepository.count()).isEqualTo(1);
        assertThat(partyJoinRepository.count()).isZero();
        assertThat(partyAgeRepository.count()).isEqualTo(2);
        assertThat(partyThumbnailUrlRepository.count()).isZero();
    }

    @DisplayName("썸네일 URL이 NULL일 때에도 직관팟을 생성할 수 있다.")
    @Test
    void createParty_NULL_IMAGE_URL() {
        // given
        Long userId = 1L;
        PartyCreateRequest request = PartyCreateRequest.of("title", "선착순", "남자만",
                List.of("10대", "20대"), 1L, 10L, null, 1L, "message");

        // when
        partyService.createParty(userId, request);

        // then
        assertThat(partyRepository.count()).isEqualTo(1);
        assertThat(partyJoinRepository.count()).isZero();
        assertThat(partyAgeRepository.count()).isEqualTo(2);
        assertThat(partyThumbnailUrlRepository.count()).isZero();
    }

    @DisplayName("이미지 저장 위한 Presigned URL 을 발급받을 수 있다.")
    @Test
    void generatePresignedUrlForSaveImage() {
        // given
        String responseUrl = "http://presigned-url.com";
        PresignedUrlForSaveImageRequest request = new PresignedUrlForSaveImageRequest("image.jpg");
        given(s3Service.generatePresignedUrl(request.imageFileName())).willReturn(responseUrl);

        // when
        PresignedUrlForSaveImageResponse result = partyService.generatePresignedUrlForSaveImage(request);

        // then
        assertThat(result.presignedUrl()).isEqualTo(responseUrl);
    }

    @DisplayName("직관팟을 수정할 수 있다.")
    @Test
    void updateParty() {
        // given
        Long userId = 1L;
        Long writerId = 1L;
        Long matchId = 1L;

        Party party = partyRepository.save(Party.create("title2", "16일 경기 같이 보실 분~", 1L, 15L,
                PartyGender.FEMALE, PartyJoinMethod.RESERVATION, writerId, matchId).assignChatRoom("TEST-CHATROOM"));

        Long partyId = party.getId();
        PartyIdRequest idRequest = PartyIdRequest.of(partyId);

        PartyUpdateRequest updateRequest = PartyUpdateRequest.of("title2", writerId, "선착순", "남자만", List.of("10대", "20대"),
                1L, 10L, List.of("newUrl"), "message");

        partyAgeRepository.saveAll(List.of(PartyAge.create(party, 10)));
        partyThumbnailUrlRepository.saveAll(List.of(PartyThumbnailUrl.create(party, "url1"), PartyThumbnailUrl.create(party, "url2")));
        partyJoinRepository.saveAll(List.of(PartyJoin.create(2L, party, PartyJoinRequestStatus.WAIT, "참여 희망합니다!")));

        // when
        PartyUpdateResponse response = partyService.updateParty(userId, idRequest, updateRequest);

        // then
        assertThat(response.partyId()).isEqualTo(partyId);

        Party result = partyRepository.findAll().get(0);
        assertThat(result).extracting("id", "title", "partyJoinMethod", "partyGender", "minimumParticipants",
                        "maximumParticipants", "currentParticipants", "writerId", "matchId", "chatRoomId", "text")
                .containsExactly(partyId, "title2", PartyJoinMethod.FIRST_COME, PartyGender.MALE, 1L,
                        10L, 1L, writerId, matchId, "TEST-CHATROOM", "message");

        List<PartyAge> ageResult = partyAgeRepository.findAll();
        assertThat(ageResult).hasSize(2)
                .extracting("age").containsExactly(10, 20);

        List<PartyThumbnailUrl> thumbnailResult = partyThumbnailUrlRepository.findAll();
        assertThat(thumbnailResult).hasSize(1)
                .extracting("thumbnailUrl")
                .containsExactly("newUrl");
    }

    @DisplayName("직관팟 작성자가 아니면 직관팟을 수정할 수 없다.")
    @Test
    void updateParty_checkWriterOrNot() {
        Long userId = 1L;
        Long writerId = 2L;
        Long matchId = 1L;
        Party party = partyRepository.save(Party.create("title2", "16일 경기 같이 보실 분~", 1L, 15L,
                PartyGender.FEMALE, PartyJoinMethod.RESERVATION, writerId, matchId).assignChatRoom("TEST-CHATROOM"));

        PartyIdRequest idRequest = PartyIdRequest.of(party.getId());
        PartyUpdateRequest updateRequest = PartyUpdateRequest.of("title2", writerId, "선착순", "남자만", List.of("10대", "20대"),
                1L, 10L, List.of("url"), "message");

        partyAgeRepository.saveAll(List.of(PartyAge.create(party, 10)));
        partyThumbnailUrlRepository.saveAll(List.of(PartyThumbnailUrl.create(party, "url1"), PartyThumbnailUrl.create(party, "url2")));
        partyJoinRepository.saveAll(List.of(PartyJoin.create(2L, party, PartyJoinRequestStatus.WAIT, "참여 희망합니다!")));

        // when // then
        assertThatThrownBy(() -> partyService.updateParty(userId, idRequest, updateRequest))
                .isInstanceOf(PartyException.NotPartyWriterException.class)
                .hasMessage("직관팟 작성자가 아니면 수정할 수 없습니다!");
    }

    @DisplayName("존재하지 않는 직관팟을 수정할 수 없다.")
    @Test
    void updateParty_NOT_EXIST_PARTY() {
        Long userId = 1L;
        Long writerId = 1L;
        Long matchId = 1L;
        PartyUpdateRequest updateRequest = PartyUpdateRequest.of("title2", writerId, "선착순", "남자만", List.of("10대", "20대"),
                1L, 10L, List.of("url"), "message");

        Party party = partyRepository.save(Party.create("title2", "16일 경기 같이 보실 분~", 1L, 15L,
                PartyGender.FEMALE, PartyJoinMethod.RESERVATION, writerId, matchId).assignChatRoom("TEST-CHATROOM"));

        partyAgeRepository.saveAll(List.of(PartyAge.create(party, 10)));
        partyThumbnailUrlRepository.saveAll(List.of(PartyThumbnailUrl.create(party, "url1"), PartyThumbnailUrl.create(party, "url2")));
        partyJoinRepository.saveAll(List.of(PartyJoin.create(2L, party, PartyJoinRequestStatus.WAIT, "참여 희망합니다!")));

        // when // then
        Long invalidPartyId = partyRepository.findAll().get(0).getId() + 1;
        assertThatThrownBy(() -> partyService.updateParty(userId, PartyIdRequest.of(invalidPartyId), updateRequest))
                .isInstanceOf(PartyException.NotFoundException.class)
                .hasMessage("잘못된 직관팟 번호입니다!");
    }

    @DisplayName("직관팟을 삭제할 수 있다.")
    @Test
    void deleteParty() {
        // given
        Long userId = 1L;
        Long writerId = 1L;
        Long matchId = 1L;

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create("CHATROOM-1"));
        List<ChatPart> chatParts = chatPartRepository.saveAll(List.of(
                ChatPart.create(userId, chatRoom.getId()), ChatPart.create(userId + 1, chatRoom.getId())));
        chatMessageRepository.saveAll(List.of(
                ChatMessage.create(chatParts.get(0).getId(), "안녕하세요!", false),
                ChatMessage.create(chatParts.get(1).getId(), "반가워요!", false)));

        Party party = partyRepository.save(Party.create("title", "설명", 1L, 10L,
                PartyGender.FEMALE, PartyJoinMethod.RESERVATION, writerId, matchId).assignChatRoom(chatRoom.getId()));
        Long partyId = party.getId();
        partyReadOnlyRepository.save(PartyDocument.createForOnlyTest(partyId, "title", "설명", 1L, 10L, 2L,
                PartyGender.FEMALE, PartyJoinMethod.RESERVATION, writerId, matchId, chatRoom.getId()));

        partyAgeRepository.saveAll(List.of(PartyAge.create(party, 10)));
        partyThumbnailUrlRepository.saveAll(List.of(PartyThumbnailUrl.create(party, "url1"),
                                                    PartyThumbnailUrl.create(party, "url2")));
        partyJoinRepository.saveAll(List.of(PartyJoin.create(2L, party, PartyJoinRequestStatus.WAIT, "참여 희망합니다!")));


        // when
        PartyDeleteResponse response = partyService.deleteParty(userId, partyId);

        // then
        assertThat(response.deletedPartyId()).isEqualTo(partyId);

        assertThat(partyRepository.count()).isZero();
        assertThat(partyThumbnailUrlRepository.count()).isZero();
        assertThat(partyJoinRepository.count()).isZero();
        assertThat(partyAgeRepository.count()).isZero();
        assertThat(partyReadOnlyRepository.count()).isEqualTo(1l);

        assertThat(chatPartRepository.count()).isZero();
        assertThat(chatRoomRepository.count()).isZero();
        assertThat(chatMessageRepository.count()).isZero();
    }

    @DisplayName("직관팟 작성자가 아니면 직관팟을 삭제할 수 없다.")
    @Test
    void deleteParty_checkWriterOrNot() {
        // given
        Long userId = 2L;
        Long writerId = 1L;
        Long matchId = 1L;

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create("CHATROOM-1"));
        List<ChatPart> chatParts = chatPartRepository.saveAll(List.of(
                ChatPart.create(userId, chatRoom.getId()), ChatPart.create(userId + 1, chatRoom.getId())));
        chatMessageRepository.saveAll(List.of(
                ChatMessage.create(chatParts.get(0).getId(), "안녕하세요!", false),
                ChatMessage.create(chatParts.get(1).getId(), "반가워요!", false)));

        Party party = partyRepository.save(Party.create("title", "설명", 1L, 10L,
                PartyGender.FEMALE, PartyJoinMethod.RESERVATION, writerId, matchId).assignChatRoom(chatRoom.getId()));
        Long partyId = party.getId();
        partyReadOnlyRepository.save(PartyDocument.createForOnlyTest(partyId, "title", "설명", 1L, 10L, 2L,
                PartyGender.FEMALE, PartyJoinMethod.RESERVATION, writerId, matchId, chatRoom.getId()));

        partyAgeRepository.saveAll(List.of(PartyAge.create(party, 10)));
        partyThumbnailUrlRepository.saveAll(List.of(PartyThumbnailUrl.create(party, "url1"),
                PartyThumbnailUrl.create(party, "url2")));
        partyJoinRepository.saveAll(List.of(PartyJoin.create(2L, party, PartyJoinRequestStatus.WAIT, "참여 희망합니다!")));

        // when // then
        assertThatThrownBy(() -> partyService.deleteParty(userId, partyId))
                .isInstanceOf(PartyException.NotPartyWriterException.class)
                .hasMessage("직관팟 작성자가 아니면 수정할 수 없습니다!");
    }

    @DisplayName("존재하지 않는 직관팟을 삭제할 수 없다.")
    @Test
    void deleteParty_NOT_EXIST_PARTY() {
        // given
        Long userId = 1L;
        Long writerId = 1L;
        Long matchId = 1L;
        Long invalidPartyId = 9999L;

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create("CHATROOM-1"));
        List<ChatPart> chatParts = chatPartRepository.saveAll(List.of(
                ChatPart.create(userId, chatRoom.getId()), ChatPart.create(userId + 1, chatRoom.getId())));
        chatMessageRepository.saveAll(List.of(
                ChatMessage.create(chatParts.get(0).getId(), "안녕하세요!", false),
                ChatMessage.create(chatParts.get(1).getId(), "반가워요!", false)));

        Party party = partyRepository.save(Party.create("title", "설명", 1L, 10L,
                PartyGender.FEMALE, PartyJoinMethod.RESERVATION, writerId, matchId).assignChatRoom(chatRoom.getId()));
        Long partyId = party.getId();
        partyReadOnlyRepository.save(PartyDocument.createForOnlyTest(partyId, "title", "설명", 1L, 10L, 2L,
                PartyGender.FEMALE, PartyJoinMethod.RESERVATION, writerId, matchId, chatRoom.getId()));

        partyAgeRepository.saveAll(List.of(PartyAge.create(party, 10)));
        partyThumbnailUrlRepository.saveAll(List.of(PartyThumbnailUrl.create(party, "url1"),
                PartyThumbnailUrl.create(party, "url2")));
        partyJoinRepository.saveAll(List.of(PartyJoin.create(2L, party, PartyJoinRequestStatus.WAIT, "참여 희망합니다!")));

        // when // then
        assertThatThrownBy(() -> partyService.deleteParty(userId, invalidPartyId))
                .isInstanceOf(PartyException.NotFoundException.class)
                .hasMessage("잘못된 직관팟 번호입니다!");
    }

}
