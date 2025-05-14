package com.playus.twpservice.global.exception;

import com.playus.twpservice.domain.party.controller.PartyController;
import com.playus.twpservice.domain.party.exception.PartyDocumentException;
import com.playus.twpservice.domain.party.exception.enums.PartyAgeGroupExceptionGroup;
import com.playus.twpservice.domain.party.exception.enums.PartyGenderExceptionGroup;
import com.playus.twpservice.domain.party.exception.enums.PartyJoinMethodExceptionGroup;
import com.playus.twpservice.global.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import software.amazon.awssdk.core.exception.SdkException;

@Slf4j
@RestControllerAdvice(assignableTypes = {
        PartyController.class
})
public class ExceptionAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ErrorResponse handleBindException(BindException e) {
        String errorMessage = e.getAllErrors().get(0).getDefaultMessage();
        log.warn("Validation Error: {}", errorMessage);
        return ErrorResponse.badRequestError(errorMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            PartyGenderExceptionGroup.InvalidDescriptionException.class,
            PartyJoinMethodExceptionGroup.InvalidDescriptionException.class,
            PartyAgeGroupExceptionGroup.InvalidDescriptionException.class
    })
    public ErrorResponse handleInvalidDescriptionException(Exception e) {
        String errorMessage = e.getMessage();
        log.warn("Validation Error: {}", errorMessage);
        return ErrorResponse.badRequestError(errorMessage);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            PartyDocumentException.NotFoundException.class
    })
    public ErrorResponse handleNotFoundException(Exception e) {
        String errorMessage = e.getMessage();
        log.error("Not Found Error: {}", errorMessage);
        return ErrorResponse.notFoundError(errorMessage);
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SdkException.class)
    public ErrorResponse handleSdkException(Exception e) {
        String errorMessage = e.getMessage();
        log.error("Object Storage Error: {}", errorMessage);
        return ErrorResponse.internalServerError("서버 에러가 발생했습니다! 관리자에게 문의해 주세요!");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleOtherException(Exception e) {
        String errorMessage = e.getMessage();
        log.error("Unexpected Error: {}", errorMessage);
        return ErrorResponse.internalServerError("서버 에러가 발생했습니다! 관리자에게 문의해 주세요!");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {
            NoFallbackAvailableException.class
    })
    public ErrorResponse handleFailOpenFeignException(NoFallbackAvailableException exception) {
        String errorMessage = exception.getMessage();
        log.error("Unexpected Error: {}", errorMessage);
        return ErrorResponse.internalServerError("서버 에러가 발생했습니다! 관리자에게 문의해 주세요!");
    }
}
