package dev.unirio.externalservice.exception;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.unirio.externalservice.exception.cobranca.PaypalApiException;

@ExtendWith(MockitoExtension.class)
class PaypalApiExceptionTest {

    @Test
    void testConstrutorComDetalhe() {
        int statusCode = 422;
    String name = "UNPROCESSABLE_ENTITY";

    PaypalApiException.PaypalErrorDetail detail = 
        new PaypalApiException.PaypalErrorDetail("INVALID_CARD", "inválido");

    PaypalApiException ex = new PaypalApiException(statusCode, name, List.of(detail));

        assertEquals(statusCode, ex.getStatusCode());
        assertEquals(name, ex.getName());
        assertEquals(1, ex.getDetails().size());
        assertEquals("INVALID_CARD", ex.getDetails().get(0).getIssue());
        assertEquals("inválido", ex.getDetails().get(0).getDescription());
    }

    @Test
    void testConstrutor() {
        String message = "Erro";
        PaypalApiException ex = new PaypalApiException(message);

        assertEquals(message, ex.getMessage());
        assertEquals(500, ex.getStatusCode()); 
        assertEquals("INTERNAL_SERVER_ERROR", ex.getName());
        assertNull(ex.getDetails());
    }
}
