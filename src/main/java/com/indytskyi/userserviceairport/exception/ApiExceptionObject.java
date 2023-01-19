package com.indytskyi.userserviceairport.exception;


import org.springframework.http.HttpStatus;

public record ApiExceptionObject(String message, HttpStatus httpStatus, String timestamp) {

}
