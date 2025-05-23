package com.playus.twpservice.domain.chat.entity;

import com.playus.twpservice.domain.common.data.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_participants")
public class ChatParticipant extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime joinedAt;

    @CreatedDate
    private LocalDateTime lastReadAt;

    private LocalDateTime disconnectedAt;

    @Builder
    private ChatParticipant(Long userId, ChatRoom chatRoom, LocalDateTime lastReadAt, LocalDateTime disconnectedAt) {
        this.userId = userId;
        this.chatRoom = chatRoom;
        this.lastReadAt = lastReadAt;
        this.disconnectedAt = disconnectedAt;
        this.joinedAt = LocalDateTime.now();
    }

    public static ChatParticipant of(ChatRoom chatRoom, Long userId) {
        return ChatParticipant.builder()
                .chatRoom(chatRoom)
                .userId(userId)
                .build();
    }

    public void reSubscribe() {
        this.lastReadAt = LocalDateTime.now();
    }

    public void unsubscribe() {
        this.lastReadAt = LocalDateTime.now();
        this.disconnectedAt = LocalDateTime.now();
    }

    public void disconnect() {
        this.lastReadAt = LocalDateTime.now();
        this.disconnectedAt = LocalDateTime.now();
    }
}
