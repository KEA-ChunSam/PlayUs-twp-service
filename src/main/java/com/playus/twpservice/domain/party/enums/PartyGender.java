package com.playus.twpservice.domain.party.enums;

import com.playus.twpservice.domain.party.exception.enums.PartyGenderExceptionGroup;
import com.playus.twpservice.domain.common.Describable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum PartyGender implements Describable {
    MALE("남자만"), FEMALE("여자만"), NO_MATTER("상관없음");

    private String description;

    public static PartyGender toEnumValue(String description) {
        return Arrays.stream(PartyGender.values())
                .filter(gender -> gender.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new PartyGenderExceptionGroup.InvalidDescriptionException("Invalid PartyGender description: " + description));
    }
}
