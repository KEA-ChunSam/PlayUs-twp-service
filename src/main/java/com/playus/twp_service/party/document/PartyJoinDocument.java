package com.playus.twp_service.party.document;

import com.playus.twp_service.party.entity.PartyJoinId;
import com.playus.twp_service.party.enums.Status;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(value = "party_join")
public class PartyJoinDocument {

    @Id
    private Long id;

//    @Id
//    private PartyJoinId id;

    @NotNull
    @Field(name = "user_id")
    private Long userId;

    @NotNull
    @DBRef(lazy = true)
    @Field(name = "party_id")
    private PartyDocument party;

    @NotNull
    private Status status;

    @NotNull
    @Field(name = "is_writer")
    private Boolean isWriter;

    @NotNull
    @Field(name = "require_message")
    @Size(min = 1, max = 100)
    private String requireMessage;

    @Builder
    private PartyJoinDocument(Long userId, PartyDocument party, Status status, Boolean isWriter, String requireMessage) {
        this.userId = userId;
        this.party = party;
        this.status = status;
        this.isWriter = isWriter;
        this.requireMessage = requireMessage;
    }

    public static PartyJoinDocument create(Long userId, PartyDocument party, Status status, String requireMessage) {
        return PartyJoinDocument.builder()
                .userId(userId)
                .party(party)
                .status(status)
                .isWriter(true)
                .requireMessage(requireMessage)
                .build();
    }
}
