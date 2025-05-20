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
@Document(collection = "party_age")
public class PartyAgeDocument {

    @Id
    private Long id;

    @NotNull
    @Indexed
    @Field(name = "party_id")
    private Long partyId;

    @NotNull
    private Integer age;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Builder
    private PartyAgeDocument(Long id, Long partyId, Integer age) {
        this.id = id;
        this.partyId = partyId;
        this.age = age;
    }

    public static PartyAgeDocument createForOnlyTest(Long id, Long partyId, Integer age) {
        return PartyAgeDocument.builder()
                .id(id)
                .partyId(partyId)
                .age(age)
                .build();
    }
}
