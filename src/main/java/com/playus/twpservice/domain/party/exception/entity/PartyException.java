package com.playus.twpservice.domain.party.exception.entity;

import java.io.Serial;

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

    public static class ExceedPartyParticipantsException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public ExceedPartyParticipantsException(String message) {
            super(message);
        }
    }

    public static class InvalidApproveRequestToPartyException extends RuntimeException {

        @Serial
        private static final long serialVersionUID = 1L;

        public InvalidApproveRequestToPartyException(String message) {
            super(message);
        }
    }
}
