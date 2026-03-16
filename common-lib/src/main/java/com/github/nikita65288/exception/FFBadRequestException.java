package com.github.nikita65288.exception;

import org.springframework.http.HttpStatus;

public class FFBadRequestException extends FFRuntimeException{
    public FFBadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
