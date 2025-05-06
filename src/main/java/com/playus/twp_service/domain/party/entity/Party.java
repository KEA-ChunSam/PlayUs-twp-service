package com.playus.twp_service.domain.party.entity;

import com.playus.twp_service.domain.common.BaseTimeEntity;
import com.playus.twp_service.domain.party.enums.PartyJoinMethod;
import com.playus.twp_service.domain.party.enums.PartyGender;
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

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(nullable = false,name = "minimum_participants")
    private Long minimumParticipants;

    @Column(nullable = false, name = "maximum_participants")
    private Long maximumParticipants;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "party_gender")
    private PartyGender partyGender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PartyJoinMethod partyJoinMethod;

    @Builder
    private Party(String title, String text, Long minimumParticipants, Long maximumParticipants, PartyGender partyGender, PartyJoinMethod partyJoinMethod){
        this.title = title;
        this.text = text;
        this.minimumParticipants = minimumParticipants;
        this.maximumParticipants = maximumParticipants;
        this.partyGender = partyGender;
        this.partyJoinMethod = partyJoinMethod;
    }

    public static Party create(String title, String text, Long minimumParticipants, Long maximumParticipants, PartyGender partyGender, PartyJoinMethod partyJoinMethod){
        return Party.builder()
                .title(title)
                .text(text)
                .minimumParticipants(minimumParticipants)
                .maximumParticipants(maximumParticipants)
                .partyGender(partyGender)
                .partyJoinMethod(partyJoinMethod)
                .build();
    }

}
