package com.playus.twp_service.party.enums;

import com.playus.twp_service.global.Describable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum PartyGender implements Describable {
    MALE("남자만"), FEMALE("여자만"), NO_MATTER("상관 없음");

    private String description;

}
