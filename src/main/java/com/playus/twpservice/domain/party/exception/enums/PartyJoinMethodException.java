package com.playus.twpservice.domain.party.exception.enums;

import java.io.Serial;

/**
 * 파티 참여 방법 (선착순, 승인제) 과 관련된 예외 그룹화
 */
public abstract class PartyJoinMethodException {

    /**
     * 유효하지 않은 참여 설명이 제공되었을 때 발생
     */
    public static class InvalidDescriptionException extends RuntimeException {

        @Serial
        private static final long serialVersionUID = 1L;

        public InvalidDescriptionException(String message) {
            super(message);
        }
    }
}
