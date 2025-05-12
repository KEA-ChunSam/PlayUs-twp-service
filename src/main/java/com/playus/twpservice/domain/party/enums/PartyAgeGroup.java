package com.playus.twpservice.domain.party.enums;

import com.playus.twpservice.domain.common.Describable;
import com.playus.twpservice.domain.party.exception.enums.PartyAgeGroupExceptionGroup;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public enum PartyAgeGroup implements Describable {
    AGE_10("10대", 10),
    AGE_20("20대", 20),
    AGE_30("30대", 30),
    AGE_40("40대", 40),
    AGE_50("50대", 50),
    AGE_60("60대 이상", 60);

    private String description;
    private int age;

    PartyAgeGroup(String description, int age) {
        this.description = description;
        this.age = age;
    }

    public static int getAgeByDescription(String description) {
        for (PartyAgeGroup group : PartyAgeGroup.values()) {
            if (group.getDescription().equals(description)) {
                return group.getAge();
            }
        }
        throw new PartyAgeGroupExceptionGroup.InvalidDescriptionException("Invalid description: " + description);
    }
}
