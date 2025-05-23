package com.playus.twpservice.domain.chat.exception.entity;

public abstract class ChatMessageException {

    public static class NotFoundException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public NotFoundException(String message) {
            super(message);
        }
    }

    public static class ChatSendEndPointException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public ChatSendEndPointException(String message) {
            super(message);
        }
    }
}
