package com.indytskyi.userserviceairport.exception.handler;

import com.indytskyi.userserviceairport.exception.ApiValidationException;
import com.indytskyi.userserviceairport.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class ApiValidationExceptionHandler {

    @ExceptionHandler
    private ResponseEntity<List<ErrorResponse>> handleException(
            ApiValidationException e) {
        return new ResponseEntity<>(e.getErrorResponses(), HttpStatus.BAD_REQUEST);
    }

}