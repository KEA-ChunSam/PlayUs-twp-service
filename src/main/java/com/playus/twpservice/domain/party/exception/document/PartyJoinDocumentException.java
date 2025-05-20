package com.playus.twpservice.domain.party.exception.document;

import java.io.Serial;

public abstract class PartyJoinDocumentException {

    public static class DuplicateApplyException extends RuntimeException {

        @Serial
        private static final long serialVersionUID = 1L;

        public DuplicateApplyException(String message) {
            super(message);
        }
    }

    public static class RefusedApplyUserException extends RuntimeException {

        @Serial
        private static final long serialVersionUID = 1L;

        public RefusedApplyUserException(String message) {
            super(message);
        }
    }
}
