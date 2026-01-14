package com.itau.desafio.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Void> httpMessageNotReadable() {
        return ResponseEntity
                .badRequest()
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Void> methodArgumentNotValid() {
        return ResponseEntity
                .unprocessableEntity()
                .build();
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public  ResponseEntity<Void> handlerMethodValidation() {
        return ResponseEntity
                .unprocessableEntity()
                .build();
    }

}