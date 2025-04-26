package com.playus.twp_service.party.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

//@Embeddable
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@EqualsAndHashCode
public class PartyJoinId implements Serializable {
    private Long userId;
    private Long partyId;
}
