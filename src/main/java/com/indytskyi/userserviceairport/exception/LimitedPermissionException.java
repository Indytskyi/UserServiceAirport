package com.indytskyi.userserviceairport.exception;

public class LimitedPermissionException extends RuntimeException {
    public LimitedPermissionException(String message) {
        super(message);
    }
}
