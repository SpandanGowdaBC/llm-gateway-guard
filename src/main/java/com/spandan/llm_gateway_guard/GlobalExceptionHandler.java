package com.spandan.llm_gateway_guard;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice // CORE: global error handler for all controllers
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class) // CORE: catches @Valid failures
    @ResponseStatus(HttpStatus.BAD_REQUEST) // CORE: HTTP 400
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("Validation failed");
        return Map.of("error", "BAD_REQUEST", "message", message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleReadableException(HttpMessageNotReadableException ex) {
        return Map.of("error", "BAD_REQUEST", "message", "Required request body is missing or malformed");
    }

    @ExceptionHandler(PromptInjectionException.class) // CORE: catches blocked injection attacks
    @ResponseStatus(HttpStatus.FORBIDDEN) // CORE: HTTP 403
    public Map<String, String> handlePromptInjection(PromptInjectionException ex) {
        return Map.of("error", "FORBIDDEN", "message", ex.getMessage());
    }
}
