package com.playus.twpservice.domain.chat.exception.entity;

public abstract class ChatParticipantException {

    public static class NotFoundException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public NotFoundException(String message) {
            super(message);
        }
    }
}
