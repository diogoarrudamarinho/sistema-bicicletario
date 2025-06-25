package unirio.pm.external_service.exception;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(PaypalApiException.class)
    public ResponseEntity<Map<String,Object>> handlePaypalException(PaypalApiException ex) {
        Map<String,Object> body = new LinkedHashMap<>();
        body.put("name", ex.getName());
        body.put("message", ex.getMessage());
        body.put("debug_id", ex.getDebug_id());
        body.put("status", ex.getStatusCode());
        if (ex.getDetails() != null) {
            List<Map<String,String>> det = ex.getDetails().stream()
                .map(d -> Map.of("issue", d.getIssue(), "description", d.getDescription()))
                .toList();
            body.put("details", det);
        }
        return ResponseEntity.status(ex.getStatusCode())
                            .body(body);
    }

    @ExceptionHandler(PaypalAuthException.class)
    public ResponseEntity<String> handlePaypalAuthException(PaypalAuthException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("AUTH_ERROR:" + ex.getMessage());
    }

}
