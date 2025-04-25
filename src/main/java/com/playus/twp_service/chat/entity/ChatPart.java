package com.playus.twp_service.chat.entity;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "chatPart")
public class ChatPart {

    @Id
    @Field(name="chat_part_id")
    private String id;

    @NotNull
    @Field(name = "user_id")
    private Long userId;

    @DBRef(lazy = true)
    @NotNull
    @Field(name = "chatroom_id")
    private ChatRoom chatRoom;

    @Builder
    private ChatPart(Long userId, ChatRoom chatRoom) {
        this.userId = userId;
        this.chatRoom = chatRoom;
    }

    public static ChatPart create(Long userId, ChatRoom chatRoom) {
        return ChatPart.builder()
                .userId(userId)
                .chatRoom(chatRoom)
                .build();
    }
}
