package com.indytskyi.userserviceairport.exception.handler;

import com.indytskyi.userserviceairport.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {
    /**
     * If we get not valid arguments.
     * @param e = object of exception
     * 
     */
    @ExceptionHandler
    private ResponseEntity<List<ErrorResponse>> handleException(MethodArgumentNotValidException e) {
        log.error("Not valid arguments, throw = {}", e.getClass());
        List<ErrorResponse> errorResponses = e.getAllErrors().stream()
                .map(a -> new ErrorResponse(Objects.requireNonNull(a.getCodes())[1]
                        .split("\\.")[1],
                        a.getDefaultMessage())).collect(Collectors.toList());

        return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
    }
    
}