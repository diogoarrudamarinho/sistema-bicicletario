package unirio.pm.external_service.exception;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import unirio.pm.external_service.exception.cobranca.PaypalApiException;
import unirio.pm.external_service.exception.cobranca.PaypalApiException.PaypalErrorDetail;
import unirio.pm.external_service.exception.cobranca.PaypalAuthException;

class GlobalExceptionHandlerTest {
     private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleValidationExceptions() {
        // Simula um erro de validação
        ObjectError error1 = new ObjectError("field1", "Campo obrigatório");
        ObjectError error2 = new ObjectError("field2", "Valor inválido");

        var bindingResult = mock(org.springframework.validation.BindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(List.of(error1, error2));

        var ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<List<String>> response = handler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals(List.of("Campo obrigatório", "Valor inválido"), response.getBody());
    }

    @Test
    void testHandlePaypalApiException() {
        PaypalErrorDetail detail = new PaypalErrorDetail("ISSUE_CODE", "Descrição do erro");
        PaypalApiException ex = new PaypalApiException(422, "PAYPAL_ERROR", List.of(detail));

        ResponseEntity<Map<String, Object>> response = handler.handlePaypalException(ex);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("PAYPAL_ERROR", body.get("name"));
        assertEquals(422, body.get("status"));

        List<?> details = (List<?>) body.get("details");
        assertEquals(1, details.size());

        Map<?, ?> detailMap = (Map<?, ?>) details.get(0);
        assertEquals("ISSUE_CODE", detailMap.get("issue"));
        assertEquals("Descrição do erro", detailMap.get("description"));
    }

    @Test
    void testHandlePaypalAuthException() {
        PaypalAuthException ex = new PaypalAuthException("Token inválido");

        ResponseEntity<String> response = handler.handlePaypalAuthException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("AUTH_ERROR:Token inválido", response.getBody());
    }

    @Test
    void testHandleObjectNotFoundException() {
        ObjectNotFoundException ex = new ObjectNotFoundException("Não encontrado", 1L);

        ResponseEntity<Map<String, Object>> response = handler.handleObjectNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(404, body.get("status"));
        assertEquals("Não encontrado", body.get("message"));
        assertEquals(1L, ex.getId());
    }
}
