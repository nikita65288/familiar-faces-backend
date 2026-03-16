package com.github.nikita65288.exception;

import org.springframework.http.HttpStatus;

public class FFUnauthorizedException extends FFRuntimeException{
    public FFUnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
