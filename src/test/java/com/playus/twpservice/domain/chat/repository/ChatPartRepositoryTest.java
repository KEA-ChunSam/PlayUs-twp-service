package com.playus.twpservice.domain.chat.repository;

import com.playus.twpservice.IntegrationTestSupport;
import com.playus.twpservice.domain.chat.entity.ChatPart;
import com.playus.twpservice.domain.chat.entity.ChatRoom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class ChatPartRepositoryTest extends IntegrationTestSupport {

    @Autowired
    ChatPartRepository chatPartRepository;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Autowired
    ChatMessageRepository chatMessageRepository;

    @AfterEach
    void tearDown() {
        chatRoomRepository.deleteAll();
        chatMessageRepository.deleteAll();
        chatPartRepository.deleteAll();
    }

    @DisplayName("채팅방 ID에 따라 chatPart를 찾을 수 있다.")
    @Test
    void findByChatRoomId() {
        // given
        Long userId = 1L;
        String roomName = "chatRoom";
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create(roomName));
        ChatRoom chatRoom1 = chatRoomRepository.save(ChatRoom.create("otherRoom"));

        chatPartRepository.saveAll(List.of(ChatPart.create(userId, chatRoom.getId()),
                ChatPart.create(userId + 1, chatRoom.getId())));

        chatPartRepository.save(ChatPart.create(userId, chatRoom1.getId()));

        // when
        List<ChatPart> result = chatPartRepository.findByChatRoomId(chatRoom.getId());

        // then
        assertThat(result).hasSize(2)
                .extracting("userId", "chatRoomId")
                .containsExactlyInAnyOrder(
                        tuple(userId, chatRoom.getId()),
                        tuple(userId + 1, chatRoom.getId())
                );
    }

}
