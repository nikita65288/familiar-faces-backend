package com.github.nikita65288.exception;

import org.springframework.http.HttpStatus;

public class FFNotFoundException extends FFRuntimeException{
    public FFNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
