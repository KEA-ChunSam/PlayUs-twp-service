package com.playus.twpservice.domain.party.entity;

import com.playus.twpservice.domain.party.enums.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "party_join")
public class PartyJoin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", nullable = false)
    private Party party;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false, name = "is_writer")
    private Boolean isWriter;

    @Column(name = "require_message", length = 100)
    private String requireMessage;

    @Builder
    private PartyJoin(Long userId, Party party, Status status, boolean isWriter, String requireMessage) {
        this.userId = userId;
        this.party = party;
        this.status = status;
        this.isWriter = isWriter;
        this.requireMessage = requireMessage;
    }

    public static PartyJoin create (Long userId, Party party, Status status,String requireMessage) {
        return PartyJoin.builder()
                .userId(userId)
                .party(party)
                .status(status)
                .isWriter(true)
                .requireMessage(requireMessage)
                .build();
    }
}
