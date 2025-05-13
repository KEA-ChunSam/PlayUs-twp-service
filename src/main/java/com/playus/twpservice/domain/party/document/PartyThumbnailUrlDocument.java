package com.playus.twpservice.domain.party.document;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = "party_thumbnailurl")
public class PartyThumbnailUrlDocument {

    @Id
    private Long id;

    @NotNull
    @Indexed
    @DocumentReference(lazy = true)
    @Field(name = "party_id")
    private PartyDocument party;

    @NotNull
    private String thumbnailUrl;

    @Builder
    private PartyThumbnailUrlDocument(Long id, PartyDocument party, String thumbnailUrl) {
        this.id = id;
        this.party = party;
        this.thumbnailUrl = thumbnailUrl;
    }

    public static PartyThumbnailUrlDocument createForOnlyTest(Long id, PartyDocument party, String thumbnailUrl) {
        return PartyThumbnailUrlDocument.builder()
                .id(id)
                .party(party)
                .thumbnailUrl(thumbnailUrl)
                .build();
    }
}
