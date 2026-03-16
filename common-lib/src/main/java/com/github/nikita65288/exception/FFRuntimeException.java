package com.github.nikita65288.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Use this to extend from for runtime exception within application
 */
@Getter
public class FFRuntimeException extends RuntimeException {

    private final HttpStatus status;

    protected FFRuntimeException(String message, HttpStatus status) {

        // Do not generate stack trace
        super(message, null, false, false);
        this.status = status;
    }
}
