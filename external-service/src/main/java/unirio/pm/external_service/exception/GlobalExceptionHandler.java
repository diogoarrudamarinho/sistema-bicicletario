package unirio.pm.external_service.exception;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import unirio.pm.external_service.exception.cobranca.PaypalApiException;
import unirio.pm.external_service.exception.cobranca.PaypalAuthException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> ((FieldError) error).getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(errors);
    }

    @ExceptionHandler(PaypalApiException.class)
    public ResponseEntity<Map<String,Object>> handlePaypalException(PaypalApiException ex) {
        Map<String,Object> body = new LinkedHashMap<>();
        body.put("name", ex.getName());
        body.put("status", ex.getStatusCode());
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

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<String> handleObjectNotFoundException(ObjectNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
