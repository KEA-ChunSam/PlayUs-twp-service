package com.playus.twpservice.domain.party.entity;

import com.playus.twpservice.domain.party.enums.PartyJoinRequestStatus;
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
@SQLDelete(sql = "UPDATE party_join SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
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
    private PartyJoinRequestStatus partyJoinRequestStatus;

    @Column(name = "require_message", length = 100)
    private String requireMessage;

    private LocalDateTime deletedAt;

    @Builder
    private PartyJoin(Long userId, Party party, PartyJoinRequestStatus partyJoinRequestStatus, String requireMessage) {
        this.userId = userId;
        this.party = party;
        this.partyJoinRequestStatus = partyJoinRequestStatus;
        this.requireMessage = requireMessage;
    }

    public static PartyJoin create (Long userId, Party party, PartyJoinRequestStatus partyJoinRequestStatus, String requireMessage) {
        return PartyJoin.builder()
                .userId(userId)
                .party(party)
                .partyJoinRequestStatus(partyJoinRequestStatus)
                .requireMessage(requireMessage)
                .build();
    }
}
