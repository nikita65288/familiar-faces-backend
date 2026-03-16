package com.github.nikita65288.exception;

import org.springframework.http.HttpStatus;

import java.io.IOException;

public class FFFileOperationException extends FFRuntimeException{

    public FFFileOperationException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
