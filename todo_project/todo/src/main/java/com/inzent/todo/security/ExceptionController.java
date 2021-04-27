package com.inzent.todo.security;

import javax.security.sasl.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler({ AuthenticationException.class })
    public ResponseEntity<String> authException(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body((e.getMessage()));
    }
}