package com.playus.twp_service.party.document;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "party_age")
public class PartyAgeDocument {

    @Id
    private Long id;

    @NotNull
    @DBRef(lazy = true)
    @Field(name = "party_id")
    private PartyDocument party;

    @NotNull
    private Integer age;

    @Builder
    private PartyAgeDocument(PartyDocument party, Integer age) {
        this.party = party;
        this.age = age;
    }

    public static PartyAgeDocument create(PartyDocument party, Integer age) {
        return PartyAgeDocument.builder()
                .party(party)
                .age(age)
                .build();
    }
}
