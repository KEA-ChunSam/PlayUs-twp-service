package com.playus.twpservice.global.exception;

import com.playus.twpservice.global.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@ControllerAdvice
public class CustomMessageExceptionHandler {

    private static final String LOG_FORMAT = "Class: {}, Code : {}, Message : {}";

    @MessageExceptionHandler(RuntimeException.class)
    @SendToUser(destinations = "/queue/errors", broadcast = false)
    public ErrorResponse handleRuntimeException(RuntimeException e) {
        String errorMessage = e.getMessage();
        log.warn(LOG_FORMAT, e.getClass().getSimpleName(), 500, errorMessage);
        return ErrorResponse.badRequestError(errorMessage);
    }
}
