package com.playus.twpservice.domain.party.enums;

import com.playus.twpservice.domain.common.validation.Describable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.playus.twpservice.domain.party.exception.enums.PartyAgeGroupException.*;


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
        throw new InvalidDescriptionException("Invalid description: " + description);
    }

    public static PartyAgeGroup getAgeGroupByAge(int age) {
        for (PartyAgeGroup group : PartyAgeGroup.values()) {
            if (group.getAge() == age) {
                return group;
            }
        }
        throw new InvalidAgeException("Invalid age: " + age);
    }

    public static String getAgeDescriptionByAge(int age) {
        for (PartyAgeGroup group : PartyAgeGroup.values()) {
            if (group.getAge() == age) {
                return group.getDescription();
            }
        }
        throw new InvalidAgeException("Invalid age: " + age);
    }
}
