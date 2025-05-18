package com.playus.twpservice.domain.party.entity;

import com.playus.twpservice.domain.common.data.BaseTimeEntity;
import com.playus.twpservice.domain.party.dto.update.PartyUpdateRequest;
import com.playus.twpservice.domain.party.enums.PartyJoinMethod;
import com.playus.twpservice.domain.party.enums.PartyGender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE party SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Table(name = "party")
public class Party extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 225)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PartyJoinMethod partyJoinMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "party_gender")
    private PartyGender partyGender;

    @Column(nullable = false,name = "minimum_participants")
    private Long minimumParticipants;

    @Column(nullable = false, name = "maximum_participants")
    private Long maximumParticipants;

    @Column(nullable = false, name = "current_participants")
    private Long currentParticipants = 1L;

    @Column(nullable = false, name = "writer_id")
    private Long writerId;

    @Column(nullable = false, name = "match_id")
    private Long matchId;

    @Column(nullable = false, name = "chatroom_id")
    private String chatRoomId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    private LocalDateTime deletedAt;

    @Builder
    private Party(Long id, String title, String text, Long minimumParticipants, Long maximumParticipants, PartyGender partyGender, PartyJoinMethod partyJoinMethod, Long writerId, Long matchId){
        this.id = id;
        this.title = title;
        this.text = text;
        this.minimumParticipants = minimumParticipants;
        this.maximumParticipants = maximumParticipants;
        this.partyGender = partyGender;
        this.partyJoinMethod = partyJoinMethod;
        this.writerId = writerId;
        this.matchId = matchId;
    }

    // setter
    public void updateParty(PartyUpdateRequest updateRequest) {
        this.title = updateRequest.title();
        this.text = updateRequest.message();
        this.minimumParticipants = updateRequest.minimumParticipants();
        this.maximumParticipants = updateRequest.maximumParticipants();
        this.partyGender = PartyGender.toEnumValue(updateRequest.partyGender());
        this.partyJoinMethod = PartyJoinMethod.toEnumValue(updateRequest.partyJoinMethod());
    }

    public Party assignChatRoom(String chatRoomId) {
        this.chatRoomId = chatRoomId;
        return this;
    }

    public static Party create(String title, String text, Long minimumParticipants, Long maximumParticipants,
                               PartyGender partyGender, PartyJoinMethod partyJoinMethod, Long writerId, Long matchId){
        return Party.builder()
                .title(title)
                .text(text)
                .minimumParticipants(minimumParticipants)
                .maximumParticipants(maximumParticipants)
                .partyGender(partyGender)
                .partyJoinMethod(partyJoinMethod)
                .writerId(writerId)
                .matchId(matchId)
                .build();
    }
}
