package com.playus.twp_service.chat.repository;

import com.playus.twp_service.chat.entity.ChatRoom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ChatRoomRepositoryTest {

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @AfterEach
    void tearDown() {
        chatRoomRepository.deleteAll();
    }

    @DisplayName("채팅방을 저장할 수 있다.")
    @Test
    void saveChatRoom() {
        // given
        String roomName = "r1";

        // when
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create(roomName));

        // then
        assertThat(chatRoom).isNotNull();
        assertThat(chatRoom.getId()).isNotNull();
        assertThat(chatRoom.getRoomName()).isEqualTo(roomName);
    }

}
