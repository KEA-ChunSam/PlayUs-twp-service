package com.playus.twpservice.domain.party.document;

import org.springframework.data.annotation.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
    private PartyAgeDocument(Long id, PartyDocument party, Integer age) {
        this.id = id;
        this.party = party;
        this.age = age;
    }

    public static PartyAgeDocument createForOnlyTest(Long id, PartyDocument party, Integer age) {
        return PartyAgeDocument.builder()
                .id(id)
                .party(party)
                .age(age)
                .build();
    }
}
