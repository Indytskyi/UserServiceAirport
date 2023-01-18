package com.indytskyi.userserviceairport.exception.handler;

import com.indytskyi.userserviceairport.exception.ApiValidationException;
import com.indytskyi.userserviceairport.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class AuthenticationExceptionHandler {

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(
            BadCredentialsException e) {
        return new ResponseEntity<>(new ErrorResponse("Authentication",
                "Your email or password is incorrect"),
                HttpStatus.FORBIDDEN);
    }
}
