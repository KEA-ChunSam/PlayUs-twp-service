package com.playus.twpservice.domain.common.security;

import com.playus.twpservice.domain.party.enums.PartyGender;

public enum Gender {
    MALE, FEMALE, UNDEFINED;

    public PartyGender toPartyGender() {
        if (this == Gender.MALE) return PartyGender.MALE;
        if (this == Gender.FEMALE) return PartyGender.FEMALE;
        throw new IllegalStateException("MALE 또는 FEMALE만 변환할 수 있습니다.");
    }
}
