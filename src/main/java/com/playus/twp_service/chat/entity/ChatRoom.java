package com.playus.twp_service.chat.entity;

import com.playus.twp_service.global.BaseTimeEntity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "chatRoom")
public class ChatRoom extends BaseTimeEntity {

    @Id
    @Field(name = "chat_room_id")
    private String id;

    @NotNull
    @Size(min = 1, max = 255)
    private String roomName;

    @Builder
    private ChatRoom(String roomName) {
        this.roomName = roomName;
    }

    public static ChatRoom create(String roomName) {
        return new ChatRoom(roomName);
    }
}
