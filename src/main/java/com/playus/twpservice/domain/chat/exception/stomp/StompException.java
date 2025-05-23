package com.playus.twpservice.domain.chat.exception.stomp;

public abstract class StompException {

    public static class ChatSubscribeException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public ChatSubscribeException(String message) {
            super(message);
        }
    }
}
