package com.playus.twpservice.global;

import com.playus.twpservice.domain.party.controller.PartyController;
import com.playus.twpservice.domain.party.exception.enums.PartyAgeGroupExceptionGroup;
import com.playus.twpservice.domain.party.exception.enums.PartyGenderExceptionGroup;
import com.playus.twpservice.domain.party.exception.enums.PartyJoinMethodExceptionGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import software.amazon.awssdk.core.exception.SdkException;

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
    @ExceptionHandler(SdkException.class)
    public ResponseEntity<String> sdkExceptionHandler(Exception e) {
        String errorMessage = e.getMessage();
        log.error("Object Storage Error: {}", errorMessage);
        return ResponseEntity.internalServerError().body("서버 에러가 발생했습니다! 관리자에게 문의해 주세요!");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> otherExceptionHandler(Exception e) {
        String errorMessage = e.getMessage();
        log.error("Unexpected Error: {}", errorMessage);
        return ResponseEntity.internalServerError().body("서버 에러가 발생했습니다! 관리자에게 문의해 주세요!");
    }
}
