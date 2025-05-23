package com.playus.twpservice.domain.chat.exception.common;

public abstract class WebSocketException {

    public static class CustomMessagingException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public CustomMessagingException(String message) {
            super(message);
        }
    }

    public static class WebSocketSessionException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public WebSocketSessionException(String message) {
            super(message);
        }
    }

    public static class TokenNotExistException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public TokenNotExistException(String message) {
            super(message);
        }
    }
}
