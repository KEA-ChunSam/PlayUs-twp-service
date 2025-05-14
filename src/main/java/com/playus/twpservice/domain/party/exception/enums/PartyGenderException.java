package com.playus.twpservice.domain.party.exception.enums;

import java.io.Serial;

/**
 * 남성, 여성 등 직관팟 성별에 관한 exception 모음입니다
 */
public abstract class PartyGenderException {

    /**
     * 잘못된 성별 문자열이 들어올 때 발생합니다
     */
    public static class InvalidDescriptionException extends RuntimeException {

        @Serial
        private static final long serialVersionUID = 1L;

        public InvalidDescriptionException(String message) {
            super(message);
        }
    }
}
