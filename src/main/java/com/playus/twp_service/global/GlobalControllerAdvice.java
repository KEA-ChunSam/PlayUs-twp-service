package com.playus.twp_service.global;

import com.playus.twp_service.party.controller.PartyController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.web.bind.support.WebExchangeBindException;

@Slf4j
@RestControllerAdvice(assignableTypes = {
        PartyController.class
})
public class GlobalControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<String> webExchangeBindExceptionHandler(WebExchangeBindException e) {
        String errorMessage = e.getAllErrors().get(0).getDefaultMessage();
        log.warn("Validation Error: {}", errorMessage);
        return ResponseEntity.badRequest().body(errorMessage);
    }
}
