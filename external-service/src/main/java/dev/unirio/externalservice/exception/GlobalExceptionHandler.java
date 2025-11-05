package dev.unirio.externalservice.exception;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.unirio.externalservice.exception.email.EmailException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailException.class)
    public ResponseEntity<Map<String, Object>> handleEmailExceptions(EmailException ex) {
         Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Erro ao enviar e-mail");
        body.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> (error).getDefaultMessage())
                .toList();

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(errors);
    }
}