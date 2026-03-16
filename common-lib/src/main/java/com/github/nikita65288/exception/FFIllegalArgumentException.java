package com.github.nikita65288.exception;

import org.springframework.http.HttpStatus;

public class FFIllegalArgumentException extends FFRuntimeException{
    public FFIllegalArgumentException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
