package com.playus.twp_service.party.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum PartyAgeGroup {
    AGE_10("10대"),
    AGE_20("20대"),
    AGE_30("30대"),
    AGE_40("40대"),
    AGE_50("50대"),
    AGE_60("60대");

    private String description;

    PartyAgeGroup(String description) {
        this.description = description;
    }

    public static PartyAgeGroup from(String description) {
        for (PartyAgeGroup partyAgeGroup : PartyAgeGroup.values()) {
            if (partyAgeGroup.getDescription().equals(description)) {
                return partyAgeGroup;
            }
        }
        throw new IllegalArgumentException("Invalid description: " + description);
    }

}
