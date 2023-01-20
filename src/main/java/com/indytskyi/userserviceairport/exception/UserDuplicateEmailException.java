package com.indytskyi.userserviceairport.exception;

public class UserDuplicateEmailException extends RuntimeException {

    public UserDuplicateEmailException(String message) {
        super(message);
    }
}