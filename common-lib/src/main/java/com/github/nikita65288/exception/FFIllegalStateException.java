package com.github.nikita65288.exception;

import org.springframework.http.HttpStatus;

public class FFIllegalStateException extends FFRuntimeException{
    public FFIllegalStateException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
