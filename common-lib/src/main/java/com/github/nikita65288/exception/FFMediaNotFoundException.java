package com.github.nikita65288.exception;

import org.springframework.http.HttpStatus;

public class FFMediaNotFoundException extends FFRuntimeException{
    public FFMediaNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
