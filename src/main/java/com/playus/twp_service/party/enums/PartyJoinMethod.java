package com.playus.twp_service.party.enums;

import com.playus.twp_service.global.Describable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum PartyJoinMethod implements Describable {

    FIRST_COME("선착순"), RESERVATION("승인제");

    private String description;
}
