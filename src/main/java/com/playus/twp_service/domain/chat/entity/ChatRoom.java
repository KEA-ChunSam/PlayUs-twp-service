package com.playus.twp_service.domain.chat.entity;

import com.playus.twp_service.domain.common.BaseTimeEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "chatRoom")
public class ChatRoom extends BaseTimeEntity {

    @Id
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
