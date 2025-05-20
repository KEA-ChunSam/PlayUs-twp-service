package com.playus.twpservice.domain.chat.entity;

import com.playus.twpservice.domain.common.data.BaseTimeEntity;
import org.springframework.data.annotation.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "chat_part")
public class ChatPart extends BaseTimeEntity {

    @Id
    private String id;

    @NotNull
    @Field(name = "user_id")
    private Long userId;

    @NotNull
    @Field(name = "chatroom_id")
    private String chatRoomId;

    @Field(name = "is_deleted")
    private Boolean isDeleted = false;

    @Field(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    private ChatPart(Long userId, String chatRoomId) {
        this.userId = userId;
        this.chatRoomId = chatRoomId;
    }

    public static ChatPart create(Long userId, String chatRoom) {
        return ChatPart.builder()
                .userId(userId)
                .chatRoomId(chatRoom)
                .build();
    }
}
