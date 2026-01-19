package com.itau.desafio.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Void> httpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.warn("Requisição com payload malformado ou ilegível: {}", ex.getMessage());
        log.debug("Detalhes da exceção: ", ex);

        return ResponseEntity
                .badRequest()
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Void> methodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<FieldError> erros = ex.getFieldErrors();

        List<String> camposInvalidos = erros.stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        log.warn("Validação de argumentos falhou: {}", camposInvalidos);
        log.debug("Detalhes da validação: ", ex);

        return ResponseEntity
                .unprocessableEntity()
                .build();
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public  ResponseEntity<Void> handlerMethodValidation(HandlerMethodValidationException ex) {
        log.warn("Validação de parâmetros de método falhou: {}", ex.getMessage());
        log.debug("Detalhes da validação: ", ex);

        return ResponseEntity
                .unprocessableEntity()
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleGenericException(Exception ex) {
        log.error("Erro inesperado na aplicação: {}", ex.getMessage(), ex);

        return ResponseEntity
                .internalServerError()
                .build();
    }

}