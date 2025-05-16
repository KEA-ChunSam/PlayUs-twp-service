package com.playus.twpservice.domain.party.entity;

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
@SQLDelete(sql = "UPDATE party_thumbnail_url SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class PartyThumbnailUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", nullable = false)
    private Party party;

    @Column(nullable = false)
    private String thumbnailUrl;

    private LocalDateTime deletedAt;

    @Builder
    private PartyThumbnailUrl(Party party, String thumbnailUrl) {
        this.party = party;
        this.thumbnailUrl = thumbnailUrl;
    }

    public static PartyThumbnailUrl create(Party party, String thumbnailUrl) {
        return PartyThumbnailUrl.builder()
                .party(party)
                .thumbnailUrl(thumbnailUrl)
                .build();
    }
}
