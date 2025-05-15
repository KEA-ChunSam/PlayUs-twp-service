package com.playus.twpservice.domain.party.exception.entity;

public abstract class PartyException {

    public static class NotPartyWriterException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public NotPartyWriterException(String message) {
            super(message);
        }
    }

    public static class NotFoundException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public NotFoundException(String message) {
            super(message);
        }
    }


}
