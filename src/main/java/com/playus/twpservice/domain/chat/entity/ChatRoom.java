package com.playus.twpservice.domain.chat.entity;

import com.playus.twpservice.domain.common.data.BaseTimeEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "chat_room")
public class ChatRoom extends BaseTimeEntity {

    @Id
    private String id;

    @NotNull
    @Size(min = 1, max = 255)
    private String roomName;

    @Field(name = "is_deleted")
    private Boolean isDeleted = false;

    @Field(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    private ChatRoom(String roomName) {
        this.roomName = roomName;
    }

    public static ChatRoom create(String roomName) {
        return new ChatRoom(roomName);
    }
}
