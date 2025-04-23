package com.playus.twp_service.party.entity;

import com.playus.twp_service.party.enums.Status;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "party_join")
public class PartyJoin {

    @EmbeddedId
    private PartyJoinId id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @MapsId("userId")
//    @JoinColumn(name = "id", nullable = false)
//    private User user;

    @Column(nullable = false, name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("partyId")
    @JoinColumn(name = "party_id", nullable = false)
    private Party party;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false, name = "is_writer")
    private boolean isWriter;

    @Column(nullable = false, name = "require_message", length = 100)
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

    public void updateAll(PartyJoin partyJoin) {
        this.userId = partyJoin.userId;
        this.party = partyJoin.party;
        this.status = partyJoin.status;
        this.isWriter = partyJoin.isWriter;
        this.requireMessage = partyJoin.requireMessage;
    }
}
