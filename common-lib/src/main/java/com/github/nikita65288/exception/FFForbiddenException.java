package com.github.nikita65288.exception;

import org.springframework.http.HttpStatus;

public class FFForbiddenException extends FFRuntimeException{
    public FFForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
