package com.playus.twpservice.domain.chat.entity;

import com.playus.twpservice.domain.common.data.BaseMongoTimeEntity;
import com.playus.twpservice.domain.common.data.BaseTimeEntity;
import org.springframework.data.annotation.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "chat_message")
public class ChatMessage extends BaseMongoTimeEntity {

    @Id
    private String id;

    @NotNull
    @Field(name = "chatpart_id")
    private String chatPartId;

    @NotBlank
    @Size(min = 1, max = 500)
    private String message;

    @NotNull
    @Field(name = "is_read")
    private Boolean isRead;

    @Field(name = "is_deleted")
    private Boolean isDeleted = false;

    @Field(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    private ChatMessage(String chatPartId, String message, Boolean isRead) {
        this.chatPartId = chatPartId;
        this.message = message;
        this.isRead = isRead;
    }

    public static ChatMessage create(String chatPartId, String message, Boolean isRead) {
        return ChatMessage.builder()
                .chatPartId(chatPartId)
                .message(message)
                .isRead(isRead)
                .build();
    }
}
