package com.example.stylescanner.error;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> handleExpiredJwtException(ExpiredJwtException e) {
        final StateResponse stateResponse = StateResponse.builder()
                .code("ERROR")
                .message("Token has expired").build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(stateResponse);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleNoSuchElementFoundException(Exception e) {

        final StateResponse stateResponse = StateResponse.builder()
                .code("ERROR")
                .message(e.getMessage()).build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(stateResponse);
    }
}
