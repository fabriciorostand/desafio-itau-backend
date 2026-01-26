package com.itau.desafio.infra.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Void> httpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.warn("Requisição com payload malformado ou ilegível: {}", ex.getMessage());

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

        return ResponseEntity
                .unprocessableEntity()
                .build();
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public  ResponseEntity<Void> handlerMethodValidation(HandlerMethodValidationException ex) {
        log.warn("Validação de parâmetros de método falhou: {}", ex.getMessage());

        return ResponseEntity
                .unprocessableEntity()
                .build();
    }

    /* Exceção lançada quando uma requisição web para um recurso estático (como imagens, CSS, JS ou JSPs)
    não consegue localizar o arquivo solicitado no caminho configurado */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Void> noResourceFound(NoResourceFoundException ex) {
        log.warn("Recurso não econtrado: {}", ex.getResourcePath());

        return ResponseEntity
                .notFound()
                .build();
    }

    /* Exceção lançada quando o Spring tenta converter um parâmetro de uma requisição para o tipo esperado por um metodo controlador,
    mas falha devido à incompatibilidade de tipos */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> methodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.warn("Parâmetro '{}' inválido, deve ser do tipo {}", ex.getName(), ex.getRequiredType().getSimpleName());

        return ResponseEntity
                .badRequest()
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleGenericException(Exception ex) {
        log.error("Erro inesperado na aplicação: ", ex);

        return ResponseEntity
                .internalServerError()
                .build();
    }
}