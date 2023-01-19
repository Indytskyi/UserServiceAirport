package com.indytskyi.userserviceairport.exception;

public class ConfirmationTokenInvalidException extends RuntimeException {
    public ConfirmationTokenInvalidException(String message) {
        super(message);
    }
}