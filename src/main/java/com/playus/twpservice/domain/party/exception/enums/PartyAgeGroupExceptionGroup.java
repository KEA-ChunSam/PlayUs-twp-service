package com.playus.twpservice.domain.party.exception.enums;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * 직관팟 나이 그룹 (10대, 20대, ...) 대한 exception을 모아놓은 곳입니다
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PartyAgeGroupExceptionGroup {

    /**
     * 잘못된 나이 문자열이 들어왔을 경우 발생합니다.
     */
    public static class InvalidDescriptionException extends RuntimeException {

        @Serial
        private static final long serialVersionUID = 1L;

        public InvalidDescriptionException(String message) {
            super(message);
        }
    }

    public static class InvalidAgeException extends RuntimeException {

        @Serial
        private static final long serialVersionUID = 1L;

        public InvalidAgeException(String message) {
            super(message);
        }
    }


}
