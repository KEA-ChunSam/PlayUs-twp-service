package com.playus.twp_service.party.document;

import com.playus.twp_service.global.BaseTimeEntity;
import com.playus.twp_service.party.enums.Method;
import com.playus.twp_service.party.enums.PartyGender;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(value = "party")
public class PartyDocument extends BaseTimeEntity {

    @Id
    @Field(name = "party_id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    private String title;

    @NotNull
    private String text;

    @NotNull
    @Field(name = "minimum_participants")
    private Long minimumParticipants;

    @NotNull
    @Field(name = "maximum_participants")
    private Long maximumParticipants;

    @NotNull
    @Field(name = "thumbnail_url")
    @Size(min = 1, max = 255)
    private String thumnailUrl;

    @NotNull
    @Field(name = "party_gender")
    private PartyGender partyGender;

    @NotNull
    private Method method;

    @Builder
    private PartyDocument(String title, String text, Long minimumParticipants, Long maximumParticipants, String thumnailUrl, PartyGender partyGender, Method method) {
        this.title = title;
        this.text = text;
        this.minimumParticipants = minimumParticipants;
        this.maximumParticipants = maximumParticipants;
        this.thumnailUrl = thumnailUrl;
        this.partyGender = partyGender;
        this.method = method;
    }

    public static PartyDocument create(String title, String text, Long minimumParticipants, Long maximumParticipants, String thumnailUrl, PartyGender partyGender, Method method) {
        return PartyDocument.builder()
                .title(title)
                .text(text)
                .minimumParticipants(minimumParticipants)
                .maximumParticipants(maximumParticipants)
                .thumnailUrl(thumnailUrl)
                .partyGender(partyGender)
                .method(method)
                .build();
    }
}
