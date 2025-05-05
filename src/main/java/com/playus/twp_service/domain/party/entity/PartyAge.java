package com.playus.twp_service.domain.party.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "party_age")
public class PartyAge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", nullable = false)
    private Party party;

    @Column(nullable = false)
    private Integer age;

    @Builder
    private PartyAge(Party party, Integer age) {
        this.party = party;
        this.age = age;
    }

    public static PartyAge create(Party party, Integer age) {
        return PartyAge.builder()
                .party(party)
                .age(age)
                .build();
    }
}
