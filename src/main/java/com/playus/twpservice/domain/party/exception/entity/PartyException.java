package com.playus.twpservice.domain.party.exception.entity;

public abstract class PartyException {

    public static class NotPartyWriterException extends RuntimeException {
        public NotPartyWriterException(String message) {
            super(message);
        }
    }

    public static class AlreadyCreatedPartyForPerMatchException extends RuntimeException {
        public AlreadyCreatedPartyForPerMatchException(String message) {
            super(message);
        }
    }

    public static class NotFoundException extends RuntimeException {
        public NotFoundException(String message) {
            super(message);
        }
    }

    public static class ParticipantsNotFoundException extends RuntimeException {
        public ParticipantsNotFoundException(String message) {
            super(message);
        }
    }

    public static class ApplicantNotFoundException extends RuntimeException {
        public ApplicantNotFoundException(String message) {
            super(message);
        }
    }

    public static class ExceedPartyParticipantsException extends RuntimeException {
        public ExceedPartyParticipantsException(String message) {
            super(message);
        }
    }

    public static class InsufficientPartyParticipantsException extends RuntimeException {
        public InsufficientPartyParticipantsException(String message) {
            super(message);
        }
    }

    public static class InvalidApproveRequestToPartyException extends RuntimeException {
        public InvalidApproveRequestToPartyException(String message) {
            super(message);
        }
    }


    public static class NotAllowedPartyConditionException extends RuntimeException {
        public NotAllowedPartyConditionException(String message) {
            super(message);
        }
    }

    public static class NotAllowedPartyJoinRequestStatusException extends RuntimeException {
        public NotAllowedPartyJoinRequestStatusException(String message) {
            super(message);
        }
    }

    public static class NotAllowedToFirstComePartyException extends RuntimeException {
        public NotAllowedToFirstComePartyException(String message) {
            super(message);
        }
    }
}
