package com.playus.twpservice.domain.party.exception.document;

import java.io.Serial;

public abstract class PartyDocumentException {

    public static class NotFoundException extends RuntimeException {

        @Serial
        private static final long serialVersionUID = 1L;

        public NotFoundException(String message) {
            super(message);
        }
    }
}
