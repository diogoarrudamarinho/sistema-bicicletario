package dev.unirio.externalservice.exception;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.unirio.externalservice.exception.cobranca.PaypalApiException;
import dev.unirio.externalservice.exception.cobranca.PaypalAuthException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String STATUS = "status";

    @ExceptionHandler(MailException.class)
    public ResponseEntity<Map<String, Object>> handleEmailExceptions(MailException ex) {
         Map<String, Object> body = new LinkedHashMap<>();
        body.put(STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
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

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleObjectNotFoundException(ObjectNotFoundException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(STATUS, HttpStatus.NOT_FOUND.value());
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(PaypalApiException.class)
    public ResponseEntity<Map<String,Object>> handlePaypalException(PaypalApiException ex) {
        Map<String,Object> body = new LinkedHashMap<>();
        body.put("name", ex.getName());
        body.put(STATUS, ex.getStatusCode());
        if (ex.getDetails() != null) {
            List<Map<String,String>> det = ex.getDetails().stream()
                .map(d -> Map.of("issue", d.getIssue(), "description", d.getDescription()))
                .toList();
            body.put("details", det);
        }
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(body);
    }

    @ExceptionHandler(PaypalAuthException.class)
    public ResponseEntity<String> handlePaypalAuthException(PaypalAuthException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("AUTH_ERROR:" + ex.getMessage());
    }

}