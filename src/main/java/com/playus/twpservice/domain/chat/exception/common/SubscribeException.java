package com.playus.twpservice.domain.chat.exception.common;

public abstract class SubscribeException {

    public static class CustomSerializationException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public CustomSerializationException(String message) {
            super(message);
        }
    }

    public static class DuplicateSubscribeException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public DuplicateSubscribeException(String message) {
            super(message);
        }
    }

    public static class RedisSubscribeException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public RedisSubscribeException(String message) {
            super(message);
        }
    }

    public static class UnSubscriptionException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public UnSubscriptionException(String message) {
            super(message);
        }
    }
}
