package com.playus.twp_service.party.entity;

import com.playus.twp_service.global.BaseTimeEntity;
import com.playus.twp_service.party.enums.Method;
import com.playus.twp_service.party.enums.PartyGender;
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

    @Column(length = 255, name = "thumbnail_url", nullable = false)
    private String thumbnailUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "party_gender")
    private PartyGender partyGender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Method method;

//    @OneToMany(mappedBy = "party")
//    private List<PartyJoin> participants;

    @Builder
    private Party(String title, String text, Long minimumParticipants, Long maximumParticipants,  String thumbnailUrl, PartyGender partyGender, Method method){
        this.title = title;
        this.text = text;
        this.minimumParticipants = minimumParticipants;
        this.maximumParticipants = maximumParticipants;
        this.thumbnailUrl = thumbnailUrl;
        this.partyGender = partyGender;
        this.method = method;
    }

    public static Party create(String title, String text, Long minimumParticipants, Long maximumParticipants,  String thumbnailUrl, PartyGender partyGender, Method method){
        return Party.builder()
                .title(title)
                .text(text)
                .minimumParticipants(minimumParticipants)
                .maximumParticipants(maximumParticipants)
                .thumbnailUrl(thumbnailUrl)
                .partyGender(partyGender)
                .method(method)
                .build();
    }

}
