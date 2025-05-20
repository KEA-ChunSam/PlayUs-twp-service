package com.playus.twpservice.domain.party.enums;

import com.playus.twpservice.domain.party.exception.enums.PartyJoinMethodException;
import com.playus.twpservice.domain.common.validation.Describable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum PartyJoinMethod implements Describable {

    FIRST_COME("선착순"), RESERVATION("승인제");

    private String description;

    public static PartyJoinMethod toEnumValue(String description) {
        return Arrays.stream(PartyJoinMethod.values())
                .filter(method -> method.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new PartyJoinMethodException.InvalidDescriptionException("Invalid PartyJoinMethod description: " + description));
    }
}
