package com.example.catalog.web;

import com.example.catalog.service.BookNotFoundException;
import com.example.catalog.service.InvalidBookException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public record ApiError(String error, String message, Instant timestamp) {}

    private ResponseEntity<ApiError> build(HttpStatus status, String code, String message) {
        return ResponseEntity.status(status)
                .body(new ApiError(code, message, Instant.now()));
    }
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ApiError> notFound(BookNotFoundException exception) {
        return build(HttpStatus.NOT_FOUND, "NOT_FOUND", exception.getMessage());
    }
    @ExceptionHandler(InvalidBookException.class)
    public ResponseEntity<ApiError> invalidBook(InvalidBookException exception) {
        return build(HttpStatus.BAD_REQUEST, "BAD_REQUEST", exception.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Entrada no válida");
        return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", message);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> unreadableJson(HttpMessageNotReadableException exception) {
        return build(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "El cuerpo JSON no es legible");
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> unexpected(Exception exception) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Unexpected error");
    }
}
