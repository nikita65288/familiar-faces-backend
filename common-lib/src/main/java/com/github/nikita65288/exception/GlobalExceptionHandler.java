package com.github.nikita65288.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
        log.error("Unexpected error: ", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleReadableException(HttpMessageNotReadableException ex, WebRequest request) {

        Throwable mostSpecificCause = NestedExceptionUtils.getMostSpecificCause(ex);

        if (mostSpecificCause instanceof FFRuntimeException ffEx) {
            log.warn("Business error found in JSON: {}", ffEx.getMessage());
            return buildResponse(ffEx.getStatus(), ffEx.getMessage(), request);
        }

        return buildResponse(HttpStatus.BAD_REQUEST, "Malformed JSON request", request);
    }

    @ExceptionHandler(FFRuntimeException.class)
    public ResponseEntity<ErrorResponse> handleFFRuntimeException(FFRuntimeException ex, WebRequest request) {

        log.warn("Business exception: {} - {}", ex.getStatus(), ex.getMessage());

        return buildResponse(ex.getStatus(), ex.getMessage(), request);
    }

    // TODO: delete?
    // Обработка ошибок валидации @Valid (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed")
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false).replace("uri=", ""))
                .errors(errors)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, WebRequest request) {
        ErrorResponse response = ErrorResponse.builder()
                .status(status.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        return new ResponseEntity<>(response, status);
    }
}
