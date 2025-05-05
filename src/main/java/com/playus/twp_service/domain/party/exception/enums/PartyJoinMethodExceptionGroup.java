package com.playus.twp_service.domain.party.exception.enums;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PartyJoinMethodExceptionGroup {

    public static class InvalidDescriptionException extends RuntimeException {
        public InvalidDescriptionException(String message) {
            super(message);
        }
    }
}
