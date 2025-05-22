package com.playus.twpservice.domain.party.document;

import com.playus.twpservice.domain.party.enums.PartyJoinRequestStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Document(value = "party_join")
public class PartyJoinDocument {

    @Id
    private Long id;

    @NotNull
    @Field(name = "user_id")
    private Long userId;

    @NotNull
    @Indexed
    @Field(name = "party_id")
    private Long partyId;

    @NotNull
    @Field(name = "party_join_request_status")
    private PartyJoinRequestStatus partyJoinRequestStatus;

    @Field(name = "require_message")
    @Size(max = 100)
    private String requireMessage;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Builder
    private PartyJoinDocument(Long id, Long userId, Long partyId, PartyJoinRequestStatus partyJoinRequestStatus, String requireMessage) {
        this.id = id;
        this.userId = userId;
        this.partyId = partyId;
        this.partyJoinRequestStatus = partyJoinRequestStatus;
        this.requireMessage = requireMessage;
    }

    public static PartyJoinDocument createForOnlyTest(Long id, Long userId, Long partyId,
                                                      PartyJoinRequestStatus partyJoinRequestStatus, String requireMessage) {
        return PartyJoinDocument.builder()
                .id(id)
                .userId(userId)
                .partyId(partyId)
                .partyJoinRequestStatus(partyJoinRequestStatus)
                .requireMessage(requireMessage)
                .build();
    }
}
