package com.playus.twp_service.global;

import com.playus.twp_service.domain.party.controller.PartyController;
import com.playus.twp_service.domain.party.exception.enums.PartyAgeGroupExceptionGroup;
import com.playus.twp_service.domain.party.exception.enums.PartyGenderExceptionGroup;
import com.playus.twp_service.domain.party.exception.enums.PartyJoinMethodExceptionGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = {
        PartyController.class
})
public class GlobalControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ResponseEntity<String> bindExceptionHandler(BindException e) {
        String errorMessage = e.getAllErrors().get(0).getDefaultMessage();
        log.warn("Validation Error: {}", errorMessage);
        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            PartyGenderExceptionGroup.InvalidDescriptionException.class,
            PartyJoinMethodExceptionGroup.InvalidDescriptionException.class,
            PartyAgeGroupExceptionGroup.InvalidDescriptionException.class
    })
    public ResponseEntity<String> invalidDescriptionExceptionHandler(Exception e) {
        String errorMessage = e.getMessage();
        log.warn("Validation Error: {}", errorMessage);
        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> otherExceptionHandler(Exception e) {
        String errorMessage = e.getMessage();
        log.error("Validation Error: {}", errorMessage);
        return ResponseEntity.badRequest().body(errorMessage);
    }


}
