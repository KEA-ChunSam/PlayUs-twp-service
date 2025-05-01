package com.playus.twp_service.global;

import com.playus.twp_service.party.controller.PartyController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.validation.BindException;

@Slf4j
@RestControllerAdvice(assignableTypes = {
        PartyController.class
})
public class GlobalControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ResponseEntity<String> bindExHandler(BindException e) {
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        String errorMessage = objectError.getDefaultMessage();

        log.warn(errorMessage);

        return ResponseEntity.badRequest().body(errorMessage);
    }
}
