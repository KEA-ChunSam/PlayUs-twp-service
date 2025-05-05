package com.playus.twp_service.domain.chat.entity;

import com.playus.twp_service.domain.common.BaseTimeEntity;
import org.springframework.data.annotation.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "chat_messages")
public class ChatMessage extends BaseTimeEntity {

    @Id
    private String id;

    @DBRef(lazy = true)
    @NotNull
    @Field(name = "chatpart_id")
    private ChatPart chatPart;

    @NotBlank
    @Size(min = 1, max = 500)
    private String message;

    @NotNull
    @Field(name = "is_read")
    private Boolean isRead;

    @Builder
    private ChatMessage(ChatPart chatPart, String message, Boolean isRead) {
        this.chatPart = chatPart;
        this.message = message;
        this.isRead = isRead;
    }

    public static ChatMessage create(ChatPart chatPart, String message, Boolean isRead) {
        return ChatMessage.builder()
                .chatPart(chatPart)
                .message(message)
                .isRead(isRead)
                .build();
    }
}
