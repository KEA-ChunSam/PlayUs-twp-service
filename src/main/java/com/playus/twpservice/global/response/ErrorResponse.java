package com.playus.twpservice.global.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ErrorResponse(
   int code,
   HttpStatus status,
   String message
) {

    public static ErrorResponse badRequestError (String message) {
        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(message)
                .build();
    }

    public static ErrorResponse unauthorizedError (String message) {
        return ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(message)
                .build();
    }

    public static ErrorResponse forbiddenError (String message) {
        return ErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN)
                .message(message)
                .build();
    }

    public static ErrorResponse notFoundError(String errorMessage) {
        return ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(errorMessage)
                .build();
    }


    public static ErrorResponse internalServerError (String message) {
        return ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(message)
                .build();
    }
}
