package com.playus.twpservice.domain.party.entity;

import com.playus.twpservice.domain.common.BaseTimeEntity;
import com.playus.twpservice.domain.party.enums.PartyJoinMethod;
import com.playus.twpservice.domain.party.enums.PartyGender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(nullable = false, name = "writer_id")
    private Long writerId;

    @Column(nullable = false, name = "match_id")
    private Long matchId;

    @Column(nullable = false, name = "chatroom_id")
    private String chatRoomId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Builder
    private Party(String title, String text, Long minimumParticipants, Long maximumParticipants, PartyGender partyGender, PartyJoinMethod partyJoinMethod, Long writerId, Long matchId){
        this.title = title;
        this.text = text;
        this.minimumParticipants = minimumParticipants;
        this.maximumParticipants = maximumParticipants;
        this.partyGender = partyGender;
        this.partyJoinMethod = partyJoinMethod;
        this.writerId = writerId;
        this.matchId = matchId;
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

    public Party assignChatRoom(String chatRoomId) {
        this.chatRoomId = chatRoomId;
        return this;
    }
}
