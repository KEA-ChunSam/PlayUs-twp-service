package com.playus.twpservice.domain.party.service;

import com.playus.twpservice.IntegrationTestSupport;
import com.playus.twpservice.domain.chat.entity.ChatMessage;
import com.playus.twpservice.domain.chat.entity.ChatPart;
import com.playus.twpservice.domain.chat.entity.ChatRoom;
import com.playus.twpservice.domain.chat.exception.ChatRoomException;
import com.playus.twpservice.domain.chat.repository.ChatMessageRepository;
import com.playus.twpservice.domain.chat.repository.ChatPartRepository;
import com.playus.twpservice.domain.chat.repository.ChatRoomRepository;
import com.playus.twpservice.domain.common.security.*;
import com.playus.twpservice.domain.party.document.PartyAgeDocument;
import com.playus.twpservice.domain.party.document.PartyDocument;
import com.playus.twpservice.domain.party.document.PartyJoinDocument;
import com.playus.twpservice.domain.party.dto.approve.PartyApproveRequest;
import com.playus.twpservice.domain.party.dto.approve.PartyApproveResponse;
import com.playus.twpservice.domain.party.dto.create.PartyCreateRequest;
import com.playus.twpservice.domain.party.dto.create.PartyCreateResponse;
import com.playus.twpservice.domain.common.request.PartyIdRequest;
import com.playus.twpservice.domain.party.dto.delete.PartyDeleteResponse;
import com.playus.twpservice.domain.party.dto.leave.PartyLeaveResponse;
import com.playus.twpservice.domain.party.dto.update.PartyUpdateRequest;
import com.playus.twpservice.domain.party.dto.presigned.PresignedUrlForSaveImageRequest;
import com.playus.twpservice.domain.party.dto.presigned.PresignedUrlForSaveImageResponse;
import com.playus.twpservice.domain.party.entity.Party;
import com.playus.twpservice.domain.party.entity.PartyAge;
import com.playus.twpservice.domain.party.entity.PartyJoin;
import com.playus.twpservice.domain.party.entity.PartyThumbnailUrl;
import com.playus.twpservice.domain.party.enums.PartyGender;
import com.playus.twpservice.domain.party.enums.PartyJoinMethod;
import com.playus.twpservice.domain.party.enums.PartyJoinRequestStatus;
import com.playus.twpservice.domain.party.exception.document.PartyJoinDocumentException;
import com.playus.twpservice.domain.party.exception.entity.PartyException;
import com.playus.twpservice.domain.party.repository.read.PartyAgeReadOnlyRepository;
import com.playus.twpservice.domain.party.repository.read.PartyJoinReadOnlyRepository;
import com.playus.twpservice.domain.party.repository.read.PartyReadOnlyRepository;
import com.playus.twpservice.domain.party.repository.write.PartyAgeRepository;
import com.playus.twpservice.domain.party.repository.write.PartyJoinRepository;
import com.playus.twpservice.domain.party.repository.write.PartyRepository;
import com.playus.twpservice.domain.party.repository.write.PartyThumbnailUrlRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
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
    private PartyJoinReadOnlyRepository partyJoinReadOnlyRepository;

    @Autowired
    private PartyAgeReadOnlyRepository partyAgeReadOnlyRepository;

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
        partyJoinReadOnlyRepository.deleteAll();
        partyAgeRepository.deleteAll();

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

//    @DisplayName("직관팟을 수정할 수 있다.")
//    @Test
//    void updateParty() {
//        // given
//        Long userId = 1L;
//        Long writerId = 1L;
//        Long matchId = 1L;
//
//        Party party = partyRepository.save(Party.create("title2", "16일 경기 같이 보실 분~", 1L, 15L,
//                PartyGender.FEMALE, PartyJoinMethod.RESERVATION, writerId, matchId).assignChatRoom("TEST-CHATROOM"));
//
//        Long partyId = party.getId();
//        PartyIdRequest idRequest = PartyIdRequest.of(partyId);
//
//        PartyUpdateRequest updateRequest = PartyUpdateRequest.of("title2", writerId, "선착순", "남자만", List.of("10대", "20대"),
//                1L, 10L, List.of("newUrl"), "message");
//
//        partyAgeRepository.saveAll(List.of(PartyAge.create(party, 10)));
//        partyThumbnailUrlRepository.saveAll(List.of(PartyThumbnailUrl.create(party, "url1"), PartyThumbnailUrl.create(party, "url2")));
//        partyJoinRepository.saveAll(List.of(PartyJoin.create(2L, party, PartyJoinRequestStatus.WAIT, "참여 희망합니다!")));
//
//        // when
//        PartyUpdateResponse response = partyService.updateParty(userId, idRequest, updateRequest);
//
//        // then
//        assertThat(response.partyId()).isEqualTo(partyId);
//
//        Party result = partyRepository.findAll().get(0);
//        assertThat(result).extracting("id", "title", "partyJoinMethod", "partyGender", "minimumParticipants",
//                        "maximumParticipants", "currentParticipants", "writerId", "matchId", "chatRoomId", "text")
//                .containsExactly(partyId, "title2", PartyJoinMethod.FIRST_COME, PartyGender.MALE, 1L,
//                        10L, 1L, writerId, matchId, "TEST-CHATROOM", "message");
//
//        List<PartyAge> ageResult = partyAgeRepository.findAll();
//        assertThat(ageResult).hasSize(2)
//                .extracting("age").containsExactly(10, 20);
//
//        List<PartyThumbnailUrl> thumbnailResult = partyThumbnailUrlRepository.findAll();
//        assertThat(thumbnailResult).hasSize(1)
//                .extracting("thumbnailUrl")
//                .containsExactly("newUrl");
//    }

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

    @DisplayName("직관팟에 선착순으로 가입할 수 있다.")
    @Test
    void applyPartyFCFS() {
        // given
        Long userId = 5L;
        UserDto userDto = UserDto.createForTest(userId, Gender.FEMALE, Role.USER, 20);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);
        Long writerId = 1L;
        Long matchId = 1L;

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create("CHATROOM-1"));

        Party party = partyRepository.save(Party.create("title", "설명", 1L, 10L,
                PartyGender.FEMALE, PartyJoinMethod.FIRST_COME, writerId, matchId).assignChatRoom(chatRoom.getId()).setCurrentParticipantsForOnlyTest(4L));

        // partyJoinDocument 통한 상태 검증 로직으로 넣음
        partyJoinReadOnlyRepository.saveAll(List.of(
                PartyJoinDocument.createForOnlyTest(1L, 2L, party.getId(), PartyJoinRequestStatus.ACCEPT, null),
                PartyJoinDocument.createForOnlyTest(2L, 3L, party.getId(), PartyJoinRequestStatus.WAIT, null),
                PartyJoinDocument.createForOnlyTest(3L, 4L, party.getId(), PartyJoinRequestStatus.REFUSE, null)));

        partyAgeReadOnlyRepository.saveAll(List.of(
                PartyAgeDocument.createForOnlyTest(1L, party.getId(), 10),
                PartyAgeDocument.createForOnlyTest(2L, party.getId(), 20)
        ));

        // when
        partyService.applyPartyFCFS(customOAuth2User, party.getId());

        // then
        assertThat(partyJoinRepository.count()).isEqualTo(1); // partyJoin 에 작성자는 존재 X
        assertThat(chatPartRepository.count()).isEqualTo(1);
        assertThat(partyRepository.findAll().get(0).getCurrentParticipants()).isEqualTo(5);
    }

    @DisplayName("자신이 만든 선착순 직관팟에 지원할 수 없다.")
    @Test
    void applyPartyFCFS_WRITER_DUPLICATE_APPLY() {
        // given
        Long writerId = 1L;
        Long matchId = 1L;
        UserDto userDto = UserDto.createForTest(writerId, Gender.FEMALE, Role.USER, 20);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create("CHATROOM-1"));

        Party party = partyRepository.save(Party.create("title", "설명", 1L, 10L,
                PartyGender.FEMALE, PartyJoinMethod.FIRST_COME, writerId, matchId).assignChatRoom(chatRoom.getId()));

        partyJoinReadOnlyRepository.saveAll(List.of(
                PartyJoinDocument.createForOnlyTest(1L, 2L, party.getId(), PartyJoinRequestStatus.ACCEPT, null),
                PartyJoinDocument.createForOnlyTest(2L, 3L, party.getId(), PartyJoinRequestStatus.WAIT, null),
                PartyJoinDocument.createForOnlyTest(3L, 4L, party.getId(), PartyJoinRequestStatus.REFUSE, null)));

        // when // then
        assertThatThrownBy(() -> partyService.applyPartyFCFS(customOAuth2User, party.getId()))
                .isInstanceOf(PartyException.NotPartyWriterException.class)
                .hasMessage("직관팟 작성자는 지원할 수 없습니다!");
    }

    @DisplayName("이미 지원한 직관팟에 다시 지원할 수 없다.")
    @Test
    void applyPartyFCFS_Duplicate_APPLY() {
        // given
        Long userId = 5L;
        UserDto userDto = UserDto.createForTest(userId, Gender.FEMALE, Role.USER, 20);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);
        Long writerId = 1L;
        Long matchId = 1L;

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create("CHATROOM-1"));

        Party party = partyRepository.save(Party.create("title", "설명", 1L, 10L,
                PartyGender.FEMALE, PartyJoinMethod.FIRST_COME, writerId, matchId).assignChatRoom(chatRoom.getId()));

        partyJoinReadOnlyRepository.saveAll(List.of(
                PartyJoinDocument.createForOnlyTest(1L, userId, party.getId(), PartyJoinRequestStatus.ACCEPT, null), // usreId 대상
                PartyJoinDocument.createForOnlyTest(2L, 2L, party.getId(), PartyJoinRequestStatus.WAIT, null),
                PartyJoinDocument.createForOnlyTest(3L, 3L, party.getId(), PartyJoinRequestStatus.REFUSE, null)));

        // when // then
        assertThatThrownBy(() -> partyService.applyPartyFCFS(customOAuth2User, party.getId()))
                .isInstanceOf(PartyJoinDocumentException.DuplicateApplyException.class)
                .hasMessage("이미 가입된 직관팟입니다!");
    }

    @DisplayName("거절된 지원팟에 다시 지원할 수 없다.")
    @Test
    void applyPartyFCFS_REFUSED_APPLY() {
        // given
        Long userId = 5L;
        UserDto userDto = UserDto.createForTest(userId, Gender.FEMALE, Role.USER, 20);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);
        Long writerId = 1L;
        Long matchId = 1L;

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create("CHATROOM-1"));

        Party party = partyRepository.save(Party.create("title", "설명", 1L, 10L,
                PartyGender.FEMALE, PartyJoinMethod.FIRST_COME, writerId, matchId).assignChatRoom(chatRoom.getId()));

        partyJoinReadOnlyRepository.saveAll(List.of(
                PartyJoinDocument.createForOnlyTest(1L, userId, party.getId(), PartyJoinRequestStatus.REFUSE, null), // 대상
                PartyJoinDocument.createForOnlyTest(2L, 2L, party.getId(), PartyJoinRequestStatus.WAIT, null),
                PartyJoinDocument.createForOnlyTest(3L, 3L, party.getId(), PartyJoinRequestStatus.REFUSE, null)));

        // when // then
        assertThatThrownBy(() -> partyService.applyPartyFCFS(customOAuth2User, party.getId()))
                .isInstanceOf(PartyJoinDocumentException.RefusedApplyUserException.class)
                .hasMessage("신청이 거절되었으면 다시 지원할 수 없습니다!");
    }

    @DisplayName("존재하지 않는 직관팟에 들어갈 수 없다.")
    @Test
    void applyPartyFCFS_INVALID_PARTY() {

        // given
        Long userId = 5L;
        UserDto userDto = UserDto.createForTest(userId, Gender.FEMALE, Role.USER, 20);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);
        Long writerId = 1L;
        Long matchId = 1L;

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create("CHATROOM-1"));

        Party party = partyRepository.save(Party.create("title", "설명", 1L, 10L,
                PartyGender.FEMALE, PartyJoinMethod.FIRST_COME, writerId, matchId).assignChatRoom(chatRoom.getId()).setCurrentParticipantsForOnlyTest(4L));

        partyJoinReadOnlyRepository.saveAll(List.of(
                PartyJoinDocument.createForOnlyTest(1L, 2L, party.getId(), PartyJoinRequestStatus.ACCEPT, null),
                PartyJoinDocument.createForOnlyTest(2L, 3L, party.getId(), PartyJoinRequestStatus.WAIT, null),
                PartyJoinDocument.createForOnlyTest(3L, 4L, party.getId(), PartyJoinRequestStatus.REFUSE, null)));

        // when // then
        assertThatThrownBy(() -> partyService.applyPartyFCFS(customOAuth2User, party.getId() - 1))
                .isInstanceOf(PartyException.NotFoundException.class)
                .hasMessage("직관팟이 존재하지 않습니다!");
    }

    @DisplayName("인원이 초과된 직관팟에는 들어갈 수 없다.")
    @Test
    void applyPartyFCFS_EXCEED_PARTICIPANTS() {

        // given
        Long userId = 5L;
        UserDto userDto = UserDto.createForTest(userId, Gender.FEMALE, Role.USER, 20);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);
        Long writerId = 1L;
        Long matchId = 1L;
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create("CHATROOM-1"));

        Party party = partyRepository.save(Party.create("title", "설명", 1L, 10L,
                        PartyGender.FEMALE, PartyJoinMethod.FIRST_COME, writerId, matchId)
                .assignChatRoom(chatRoom.getId()).setCurrentParticipantsForOnlyTest(10L));

        partyJoinReadOnlyRepository.saveAll(List.of(
                PartyJoinDocument.createForOnlyTest(1L, 2L, party.getId(), PartyJoinRequestStatus.ACCEPT, null),
                PartyJoinDocument.createForOnlyTest(2L, 3L, party.getId(), PartyJoinRequestStatus.WAIT, null),
                PartyJoinDocument.createForOnlyTest(3L, 4L, party.getId(), PartyJoinRequestStatus.REFUSE, null)));

        // when // then
        assertThatThrownBy(() -> partyService.applyPartyFCFS(customOAuth2User, party.getId()))
                .isInstanceOf(PartyException.ExceedPartyParticipantsException.class)
                .hasMessage("직관팟 정원이 초과되었습니다!");
    }

    @DisplayName("존재하지 않는 채팅방에는 들어갈 수 없다.")
    @Test
    void applyPartyFCFS_INVALID_CHATROOM() {

        // given
        Long userId = 5L;
        UserDto userDto = UserDto.createForTest(userId, Gender.FEMALE, Role.USER, 20);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);
        Long writerId = 1L;
        Long matchId = 1L;

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create("CHATROOM-1"));

        Party party = partyRepository.save(Party.create("title", "설명", 1L, 10L,
                PartyGender.FEMALE, PartyJoinMethod.FIRST_COME, writerId, matchId).assignChatRoom(chatRoom.getId() + "a"));

        partyJoinReadOnlyRepository.saveAll(List.of(
                PartyJoinDocument.createForOnlyTest(1L, 2L, party.getId(), PartyJoinRequestStatus.ACCEPT, null),
                PartyJoinDocument.createForOnlyTest(2L, 3L, party.getId(), PartyJoinRequestStatus.WAIT, null),
                PartyJoinDocument.createForOnlyTest(3L, 4L, party.getId(), PartyJoinRequestStatus.REFUSE, null)));

        partyAgeReadOnlyRepository.saveAll(List.of(
                PartyAgeDocument.createForOnlyTest(1L, party.getId(), 10),
                PartyAgeDocument.createForOnlyTest(2L, party.getId(), 20)
        ));

        // when // then
        assertThatThrownBy(() -> partyService.applyPartyFCFS(customOAuth2User, party.getId()))
                .isInstanceOf(ChatRoomException.NotFoundException.class)
                .hasMessage("채팅방이 존재하지 않습니다!");
    }

    @DisplayName("승인제 직관팟에 가입할 수 있다.")
    @Test
    void applyParty() {
        // given
        Long userId = 5L;
        UserDto userDto = UserDto.createForTest(userId, Gender.FEMALE, Role.USER, 20);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);
        Long writerId = 1L;
        Long matchId = 1L;

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create("CHATROOM-1"));

        Party party = partyRepository.save(Party.create("title", "설명", 1L, 10L,
                PartyGender.FEMALE, PartyJoinMethod.RESERVATION, writerId, matchId).assignChatRoom(chatRoom.getId()));

        partyJoinReadOnlyRepository.saveAll(List.of(
                PartyJoinDocument.createForOnlyTest(1L, 2L, party.getId(), PartyJoinRequestStatus.ACCEPT, null),
                PartyJoinDocument.createForOnlyTest(2L, 3L, party.getId(), PartyJoinRequestStatus.WAIT, null),
                PartyJoinDocument.createForOnlyTest(3L, 4L, party.getId(), PartyJoinRequestStatus.REFUSE, null)));

        partyAgeReadOnlyRepository.saveAll(List.of(
                PartyAgeDocument.createForOnlyTest(1L, party.getId(), 10),
                PartyAgeDocument.createForOnlyTest(2L, party.getId(), 20)
        ));

        // when
        partyService.applyParty(customOAuth2User, party.getId(), "참여 희망합니다!");

        // then
        assertThat(partyJoinRepository.count()).isEqualTo(1); // partyJoin 에 작성자는 존재 X
        assertThat(chatPartRepository.count()).isEqualTo(0);
        assertThat(partyRepository.findAll().get(0).getCurrentParticipants()).isEqualTo(1);

        assertThat(partyJoinRepository.findAll().get(0))
                .extracting("partyJoinRequestStatus", "requireMessage")
                .containsExactly(PartyJoinRequestStatus.WAIT, "참여 희망합니다!");
    }

    @DisplayName("자신이 만든 승인제 직관팟에 지원할 수 없다.")
    @Test
    void applyParty_WRITER_DUPLICATE_APPLY() {
        // given
        Long writerId = 1L;
        Long matchId = 1L;
        UserDto userDto = UserDto.createForTest(writerId, Gender.FEMALE, Role.USER, 20);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create("CHATROOM-1"));

        Party party = partyRepository.save(Party.create("title", "설명", 1L, 10L,
                PartyGender.FEMALE, PartyJoinMethod.RESERVATION, writerId, matchId).assignChatRoom(chatRoom.getId()));

        partyJoinReadOnlyRepository.saveAll(List.of(
                PartyJoinDocument.createForOnlyTest(1L, 2L, party.getId(), PartyJoinRequestStatus.ACCEPT, null),
                PartyJoinDocument.createForOnlyTest(2L, 3L, party.getId(), PartyJoinRequestStatus.WAIT, null),
                PartyJoinDocument.createForOnlyTest(3L, 4L, party.getId(), PartyJoinRequestStatus.REFUSE, null)));

        // when // then
        assertThatThrownBy(() -> partyService.applyParty(customOAuth2User, party.getId(), null))
                .isInstanceOf(PartyException.NotPartyWriterException.class)
                .hasMessage("직관팟 작성자는 지원할 수 없습니다!");
    }

    @DisplayName("이미 지원한 승인제 직관팟에 다시 지원할 수 없다.")
    @Test
    void applyParty_Duplicate_APPLY() {
        // given
        Long userId = 5L;
        UserDto userDto = UserDto.createForTest(userId, Gender.FEMALE, Role.USER, 20);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);
        Long writerId = 1L;
        Long matchId = 1L;


        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create("CHATROOM-1"));

        Party party = partyRepository.save(Party.create("title", "설명", 1L, 10L,
                PartyGender.FEMALE, PartyJoinMethod.RESERVATION, writerId, matchId).assignChatRoom(chatRoom.getId()));

        partyJoinReadOnlyRepository.saveAll(List.of(
                PartyJoinDocument.createForOnlyTest(1L, userId, party.getId(), PartyJoinRequestStatus.ACCEPT, null), // usreId 대상
                PartyJoinDocument.createForOnlyTest(2L, 2L, party.getId(), PartyJoinRequestStatus.WAIT, null),
                PartyJoinDocument.createForOnlyTest(3L, 3L, party.getId(), PartyJoinRequestStatus.REFUSE, null)));

        // when // then
        assertThatThrownBy(() -> partyService.applyParty(customOAuth2User, party.getId(), null))
                .isInstanceOf(PartyJoinDocumentException.DuplicateApplyException.class)
                .hasMessage("이미 가입된 직관팟입니다!");
    }

    @DisplayName("거절된 승인제 지원팟에 다시 지원할 수 없다.")
    @Test
    void applyParty_REFUSED_APPLY() {
        // given
        Long userId = 5L;
        UserDto userDto = UserDto.createForTest(userId, Gender.FEMALE, Role.USER, 20);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);
        Long writerId = 1L;
        Long matchId = 1L;

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create("CHATROOM-1"));

        Party party = partyRepository.save(Party.create("title", "설명", 1L, 10L,
                PartyGender.FEMALE, PartyJoinMethod.RESERVATION, writerId, matchId).assignChatRoom(chatRoom.getId()));

        partyJoinReadOnlyRepository.saveAll(List.of(
                PartyJoinDocument.createForOnlyTest(1L, userId, party.getId(), PartyJoinRequestStatus.REFUSE, null), // 대상
                PartyJoinDocument.createForOnlyTest(2L, 2L, party.getId(), PartyJoinRequestStatus.WAIT, null),
                PartyJoinDocument.createForOnlyTest(3L, 3L, party.getId(), PartyJoinRequestStatus.REFUSE, null)));

        // when // then
        assertThatThrownBy(() -> partyService.applyParty(customOAuth2User, party.getId(), null))
                .isInstanceOf(PartyJoinDocumentException.RefusedApplyUserException.class)
                .hasMessage("신청이 거절되었으면 다시 지원할 수 없습니다!");
    }

    @DisplayName("참여 메시지가 없어도 승인제 직관팟에 가입할 수 있다.")
    @NullAndEmptySource
    @ParameterizedTest
    void applyParty_without_requiremessage(String emptyRequireMessage) {
        // given
        Long userId = 5L;
        UserDto userDto = UserDto.createForTest(userId, Gender.FEMALE, Role.USER, 20);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);
        Long writerId = 1L;
        Long matchId = 1L;

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create("CHATROOM-1"));

        Party party = partyRepository.save(Party.create("title", "설명", 1L, 10L,
                PartyGender.FEMALE, PartyJoinMethod.RESERVATION, writerId, matchId).assignChatRoom(chatRoom.getId()));

        partyJoinReadOnlyRepository.saveAll(List.of(
                PartyJoinDocument.createForOnlyTest(1L, 2L, party.getId(), PartyJoinRequestStatus.ACCEPT, null),
                PartyJoinDocument.createForOnlyTest(2L, 3L, party.getId(), PartyJoinRequestStatus.WAIT, null),
                PartyJoinDocument.createForOnlyTest(3L, 4L, party.getId(), PartyJoinRequestStatus.REFUSE, null)));

        partyAgeReadOnlyRepository.saveAll(List.of(
                PartyAgeDocument.createForOnlyTest(1L, party.getId(), 10),
                PartyAgeDocument.createForOnlyTest(2L, party.getId(), 20)
        ));

        // when
        partyService.applyParty(customOAuth2User, party.getId(), emptyRequireMessage);

        // then
        assertThat(partyJoinRepository.count()).isEqualTo(1); // partyJoin 에 작성자는 존재 X
        assertThat(chatPartRepository.count()).isEqualTo(0);
        assertThat(partyRepository.findAll().get(0).getCurrentParticipants()).isEqualTo(1);

        assertThat(partyJoinRepository.findAll().get(0))
                .extracting("partyJoinRequestStatus", "requireMessage")
                .containsExactly(PartyJoinRequestStatus.WAIT, emptyRequireMessage);
    }

    @DisplayName("존재하지 않는 승인제 직관팟에 들어갈 수 없다.")
    @Test
    void applyParty_INVALID_PARTY() {

        // given
        Long userId = 5L;
        UserDto userDto = UserDto.createForTest(userId, Gender.FEMALE, Role.USER, 20);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);
        Long writerId = 1L;
        Long matchId = 1L;

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create("CHATROOM-1"));

        Party party = partyRepository.save(Party.create("title", "설명", 1L, 10L,
                PartyGender.FEMALE, PartyJoinMethod.RESERVATION, writerId, matchId).assignChatRoom(chatRoom.getId()));


        // when // then
        assertThatThrownBy(() -> partyService.applyParty(customOAuth2User, party.getId() - 1, null))
                .isInstanceOf(PartyException.NotFoundException.class)
                .hasMessage("직관팟이 존재하지 않습니다!");
    }

    @DisplayName("인원이 초과된 승인제 직관팟에는 들어갈 수 없다.")
    @Test
    void applyParty_EXCEED_PARTICIPANTS() {

        // given
        Long userId = 5L;
        UserDto userDto = UserDto.createForTest(userId, Gender.FEMALE, Role.USER, 20);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);
        Long writerId = 1L;
        Long matchId = 1L;

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create("CHATROOM-1"));

        Party party = partyRepository.save(Party.create("title", "설명", 1L, 10L,
                        PartyGender.FEMALE, PartyJoinMethod.RESERVATION, writerId, matchId)
                .assignChatRoom(chatRoom.getId()).setCurrentParticipantsForOnlyTest(10L));

        // when // then
        assertThatThrownBy(() -> partyService.applyParty(customOAuth2User, party.getId(), "참여 희망합니다!"))
                .isInstanceOf(PartyException.ExceedPartyParticipantsException.class)
                .hasMessage("직관팟 정원이 초과되었습니다!");
    }

    @DisplayName("승인제 직관팟에 대한 참여 요청을 승인할 수 있다.")
    @Test
    void approveParty() {
        // given
        Long loginUserId = 1L;
        Long applicantUserId = 2L;
        PartyApproveRequest request = PartyApproveRequest.of(applicantUserId, true);

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create("CHATROOM-1"));
        Party party = partyRepository.save(Party.create("title", "설명", 1L, 10L,
                PartyGender.MALE, PartyJoinMethod.RESERVATION, loginUserId, 1L).assignChatRoom(chatRoom.getId()));
        Long partyId = party.getId();

        partyJoinRepository.saveAll(List.of(
                PartyJoin.create(applicantUserId, party, PartyJoinRequestStatus.WAIT, "참여 희망합니다!")
        ));

        // when
        PartyApproveResponse response = partyService.approveParty(loginUserId, partyId, request);

        // then
        assertThat(response.message()).isEqualTo("직관팟 가입 신청 승인 성공했습니다!");
        assertThat(partyJoinRepository.findAll().get(0))
                .extracting("partyJoinRequestStatus", "requireMessage")
                .containsExactly(PartyJoinRequestStatus.ACCEPT, "참여 희망합니다!");
        assertThat(chatPartRepository.findAll().get(0))
                .extracting("userId", "chatRoomId")
                .containsExactly(applicantUserId, chatRoom.getId());
    }

    @DisplayName("승인제 직관팟에 대한 참여 요청을 거절할 수 있다.")
    @Test
    void approveParty_REFUSE() {
        // given
        Long loginUserId = 1L;
        Long applicantUserId = 2L;
        PartyApproveRequest request = PartyApproveRequest.of(applicantUserId, false);

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create("CHATROOM-1"));
        Party party = partyRepository.save(Party.create("title", "설명", 1L, 10L,
                PartyGender.MALE, PartyJoinMethod.RESERVATION, loginUserId, 1L).assignChatRoom(chatRoom.getId()));
        Long partyId = party.getId();

        partyJoinRepository.saveAll(List.of(
                PartyJoin.create(applicantUserId, party, PartyJoinRequestStatus.WAIT, "참여 희망합니다!")
        ));

        // when
        PartyApproveResponse response = partyService.approveParty(loginUserId, partyId, request);

        // then
        assertThat(response.message()).isEqualTo("직관팟 가입 신청 거절 성공했습니다!");
        assertThat(partyJoinRepository.findAll().get(0))
                .extracting("partyJoinRequestStatus", "requireMessage")
                .containsExactly(PartyJoinRequestStatus.REFUSE, "참여 희망합니다!");
        assertThat(chatPartRepository.count()).isZero();
    }

    @DisplayName("존재하지 않는 승인제 직관팟에 대해서는 승인 요청을 날릴 수 없다..")
    @Test
    void approveParty_NOT_FOUND() {
        // given
        Long loginUserId = 1L;
        Long applicantUserId = 2L;
        PartyApproveRequest request = PartyApproveRequest.of(applicantUserId, true);

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create("CHATROOM-1"));
        Party party = partyRepository.save(Party.create("title", "설명", 1L, 10L,
                PartyGender.MALE, PartyJoinMethod.RESERVATION, loginUserId, 1L).assignChatRoom(chatRoom.getId()));
        partyJoinRepository.saveAll(List.of(
                PartyJoin.create(applicantUserId, party, PartyJoinRequestStatus.WAIT, "참여 희망합니다!")
        ));

        // when // then
        assertThatThrownBy(() -> partyService.approveParty(loginUserId, party.getId() + 1, request))
                .isInstanceOf(PartyException.NotFoundException.class)
                .hasMessage("직관팟이 존재하지 않습니다!");
    }

    @DisplayName("선착순 직관팟에 대해 승인 요청을 날릴 수 없다.")
    @Test
    void approveParty_NOT_FCFS() {
        // given
        Long loginUserId = 1L;
        Long applicantUserId = 2L;
        PartyApproveRequest request = PartyApproveRequest.of(applicantUserId, true);

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create("CHATROOM-1"));
        Party party = partyRepository.save(Party.create("title", "설명", 1L, 10L,
                PartyGender.MALE, PartyJoinMethod.FIRST_COME, loginUserId, 1L).assignChatRoom(chatRoom.getId()));
        Long partyId = party.getId();

        partyJoinRepository.saveAll(List.of(
                PartyJoin.create(applicantUserId, party, PartyJoinRequestStatus.WAIT, "참여 희망합니다!")
        ));

        // when // then
        assertThatThrownBy(() -> partyService.approveParty(loginUserId, partyId, request))
                .isInstanceOf(PartyException.InvalidApproveRequestToPartyException.class)
                .hasMessage("선착순 모집인 직관팟에는 승인 요청을 보낼 수 없습니다!");
    }

    @DisplayName("오직 작성자만 직관팟에 대해 승인할 수 있습니다.")
    @Test
    void approveParty_ONLY_WRITER_CAN_APPROVE() {
        // given
        Long loginUserId = 1L;
        Long applicantUserId = 2L;
        PartyApproveRequest request = PartyApproveRequest.of(applicantUserId, true);

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create("CHATROOM-1"));
        Party party = partyRepository.save(Party.create("title", "설명", 1L, 10L,
                PartyGender.MALE, PartyJoinMethod.RESERVATION, loginUserId + 1, 1L).assignChatRoom(chatRoom.getId()));
        Long partyId = party.getId();

        partyJoinRepository.saveAll(List.of(
                PartyJoin.create(applicantUserId, party, PartyJoinRequestStatus.WAIT, "참여 희망합니다!")
        ));

        // when // then
        assertThatThrownBy(() -> partyService.approveParty(loginUserId, partyId, request))
                .isInstanceOf(PartyException.NotPartyWriterException.class)
                .hasMessage("작성자가 아니면 승인할 수 없습니다!");
    }

    @DisplayName("직관팟 지원자가 아닌 경우에는 승인할 수 없다.")
    @Test
    void approveParty_NOT_APPLICANT() {
        // given
        Long loginUserId = 1L;
        Long applicantUserId = 2L;
        PartyApproveRequest request = PartyApproveRequest.of(applicantUserId, true);

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create("CHATROOM-1"));
        Party party = partyRepository.save(Party.create("title", "설명", 1L, 10L,
                PartyGender.MALE, PartyJoinMethod.RESERVATION, loginUserId, 1L).assignChatRoom(chatRoom.getId()));
        Long partyId = party.getId();

        partyJoinRepository.saveAll(List.of(
                PartyJoin.create(applicantUserId + 1, party, PartyJoinRequestStatus.WAIT, "참여 희망합니다!")
        ));

        // when // then
        assertThatThrownBy(() -> partyService.approveParty(loginUserId, partyId, request))
                .isInstanceOf(PartyException.NotFoundException.class)
                .hasMessage("직관팟 지원자가 아닙니다!");
    }

    @DisplayName("직관팟을 탈퇴할 수 있다.")
    @Test
    void leaveParty() {
        // given
        Long userId = 5L;
        UserDto userDto = UserDto.createForTest(userId, Gender.FEMALE, Role.USER, 20);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);
        Long writerId = 1L;
        Long matchId = 1L;

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create("CHATROOM-1"));

        Party party = partyRepository.save(Party.create("title", "설명", 1L, 10L,
                        PartyGender.FEMALE, PartyJoinMethod.RESERVATION, writerId, matchId)
                .assignChatRoom(chatRoom.getId()).setCurrentParticipantsForOnlyTest(2L));

        partyJoinRepository.saveAll(
                List.of(
                        PartyJoin.create(userId, party, PartyJoinRequestStatus.ACCEPT, "참여 희망합니다!"),
                        PartyJoin.create(userId + 1, party, PartyJoinRequestStatus.ACCEPT, "참여 원합니다!")
                )
        );

        // when
        PartyLeaveResponse response = partyService.leaveParty(customOAuth2User, party.getId());

        // then
        assertThat(response.message()).isEqualTo("직관팟 탈퇴에 성공하셨습니다!");
        assertThat(partyJoinRepository.count()).isEqualTo(1);
        assertThat(partyRepository.findAll().get(0).getCurrentParticipants()).isEqualTo(1);
    }

    @DisplayName("존재하지 않는 직관팟에 대해 탈퇴할 수 없다.")
    @Test
    void leaveParty_INVALID_PARTY() {
        // given
        Long writerId = 1L;
        Long userId = 5L;
        UserDto userDto = UserDto.createForTest(userId, Gender.FEMALE, Role.USER, 20);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);
        Long matchId = 1L;

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create("CHATROOM-1"));

        Party party = partyRepository.save(Party.create("title", "설명", 1L, 10L,
                        PartyGender.FEMALE, PartyJoinMethod.RESERVATION, writerId, matchId)
                .assignChatRoom(chatRoom.getId()).setCurrentParticipantsForOnlyTest(2L));

        partyJoinRepository.saveAll(
                List.of(
                        PartyJoin.create(writerId, party, PartyJoinRequestStatus.ACCEPT, "참여 원합니다!")
                )
        );

        // when // then
        assertThatThrownBy(() -> partyService.leaveParty(customOAuth2User, party.getId() + 1))
                .isInstanceOf(PartyException.NotFoundException.class)
                .hasMessage("직관팟이 존재하지 않습니다!");
    }

    @DisplayName("직관팟 방장은 직관팟을 탈퇴할 수 없다.")
    @Test
    void leaveParty_WRITER_NOT_ALLOWED() {
        // given
        Long writerId = 1L;
        UserDto userDto = UserDto.createForTest(writerId, Gender.FEMALE, Role.USER, 20);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);
        Long matchId = 1L;

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create("CHATROOM-1"));

        Party party = partyRepository.save(Party.create("title", "설명", 1L, 10L,
                        PartyGender.FEMALE, PartyJoinMethod.RESERVATION, writerId, matchId)
                .assignChatRoom(chatRoom.getId()).setCurrentParticipantsForOnlyTest(10L));

        partyJoinRepository.saveAll(
                List.of(
                        PartyJoin.create(writerId + 1, party, PartyJoinRequestStatus.ACCEPT, "참여 원합니다!")
                )
        );

        // when // then
        assertThatThrownBy(() -> partyService.leaveParty(customOAuth2User, party.getId()))
                .isInstanceOf(PartyException.NotPartyWriterException.class)
                .hasMessage("방장은 직관팟을 삭제해 주세요!");
    }

    @DisplayName("참여하지 않은 직관팟에 대해 탈퇴할 수 없다.")
    @Test
    void leaveParty_NOT_PARTICIPATED() {
        // given
        Long userId = 5L;
        UserDto userDto = UserDto.createForTest(userId, Gender.FEMALE, Role.USER, 20);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);
        Long writerId = 1L;
        Long matchId = 1L;

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create("CHATROOM-1"));

        Party party = partyRepository.save(Party.create("title", "설명", 1L, 10L,
                        PartyGender.FEMALE, PartyJoinMethod.RESERVATION, writerId, matchId)
                .assignChatRoom(chatRoom.getId()).setCurrentParticipantsForOnlyTest(10L));


        // when // then
        assertThatThrownBy(() -> partyService.leaveParty(customOAuth2User, party.getId()))
                .isInstanceOf(PartyException.ApplicantNotFoundException.class)
                .hasMessage("직관팟에 참여한 사람만 탈퇴할 수 있습니다!");
    }

    @DisplayName("대기 상태인 유저는 직관팟을 탈퇴할 수 없다.")
    @Test
    void leaveParty_WAIT_USER_NOT_ALLOWED() {
        Long writerId = 1L;
        Long userId = 5L;
        UserDto userDto = UserDto.createForTest(userId, Gender.FEMALE, Role.USER, 20);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);
        Long matchId = 1L;

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create("CHATROOM-1"));

        Party party = partyRepository.save(Party.create("title", "설명", 1L, 10L,
                        PartyGender.FEMALE, PartyJoinMethod.RESERVATION, writerId, matchId)
                .assignChatRoom(chatRoom.getId()).setCurrentParticipantsForOnlyTest(10L));

        partyJoinRepository.saveAll(
                List.of(
                        PartyJoin.create(userId, party, PartyJoinRequestStatus.WAIT, "참여 원합니다!")
                )
        );

        // when // then
        assertThatThrownBy(() -> partyService.leaveParty(customOAuth2User, party.getId()))
                .isInstanceOf(PartyException.NotAllowedPartyJoinRequestStatusException.class)
                .hasMessage("대기 상태인 유저는 직관팟 신청을 취소해주세요!");
    }

    @DisplayName("직관팟 참여가 거절된 유저는 직관팟을 탈퇴할 수 없다.")
    @Test
    void leaveParty_REFUSED_NOT_ALLOWED() {
        Long writerId = 1L;
        Long userId = 5L;
        UserDto userDto = UserDto.createForTest(userId, Gender.FEMALE, Role.USER, 20);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);
        Long matchId = 1L;

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create("CHATROOM-1"));

        Party party = partyRepository.save(Party.create("title", "설명", 1L, 10L,
                        PartyGender.FEMALE, PartyJoinMethod.RESERVATION, writerId, matchId)
                .assignChatRoom(chatRoom.getId()).setCurrentParticipantsForOnlyTest(10L));

        partyJoinRepository.saveAll(
                List.of(
                        PartyJoin.create(userId, party, PartyJoinRequestStatus.REFUSE, "참여 원합니다!")
                )
        );

        // when // then
        assertThatThrownBy(() -> partyService.leaveParty(customOAuth2User, party.getId()))
                .isInstanceOf(PartyException.NotAllowedPartyJoinRequestStatusException.class)
                .hasMessage("직관팟 참여가 이미 거절되었습니다!");
    }

//    @DisplayName("직관팟 참여가 최소 인원인 경우 직관팟을 탈퇴할 수 없다?")
//    @Test
//    void leaveParty_REFUSED_NOT_ALLOWED() {
//        Long writerId = 1L;
//        Long userId = 5L;
//        UserDto userDto = UserDto.createForTest(userId, Gender.FEMALE, Role.USER, 20);
//        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);
//        Long matchId = 1L;
//
//        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create("CHATROOM-1"));
//
//        Party party = partyRepository.save(Party.create("title", "설명", 1L, 10L,
//                        PartyGender.FEMALE, PartyJoinMethod.RESERVATION, writerId, matchId)
//                .assignChatRoom(chatRoom.getId()).setCurrentParticipantsForOnlyTest(10L));
//
//        partyJoinRepository.saveAll(
//                List.of(
//                        PartyJoin.create(writerId + 1, party, PartyJoinRequestStatus.REFUSE, "참여 원합니다!")
//                )
//        );
//
//        // when // then
//        assertThatThrownBy(() -> partyService.leaveParty(customOAuth2User, party.getId()))
//                .isInstanceOf(PartyException.NotAllowedPartyJoinRequestStatusException.class)
//                .hasMessage("직관팟 참여가 이미 거절되었습니다!");
//    }
}
