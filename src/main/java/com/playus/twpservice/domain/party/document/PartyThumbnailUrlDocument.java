package com.playus.twpservice.domain.party.document;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = "party_thumbnailurl")
public class PartyThumbnailUrlDocument {

    @Id
    private Long id;

    @NotNull
    @Indexed
    @Field(name = "party_id")
    private Long partyId;

    @NotNull
    private String thumbnailUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Builder
    private PartyThumbnailUrlDocument(Long id, Long partyId, String thumbnailUrl) {
        this.id = id;
        this.partyId = partyId;
        this.thumbnailUrl = thumbnailUrl;
    }

    public static PartyThumbnailUrlDocument createForOnlyTest(Long id, Long partyId, String thumbnailUrl) {
        return PartyThumbnailUrlDocument.builder()
                .id(id)
                .partyId(partyId)
                .thumbnailUrl(thumbnailUrl)
                .build();
    }
}
