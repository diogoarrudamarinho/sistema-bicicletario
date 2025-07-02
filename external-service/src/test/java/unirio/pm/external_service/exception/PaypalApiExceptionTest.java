package unirio.pm.external_service.exception;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import unirio.pm.external_service.exception.cobranca.PaypalApiException;

@ExtendWith(MockitoExtension.class)
public class PaypalApiExceptionTest {

    @Test
    void testConstrutorComDetalhe() {
        int statusCode = 422;
        String name = "UNPROCESSABLE_ENTITY";
        
        PaypalApiException.PaypalErrorDetail detail = new PaypalApiException.PaypalErrorDetail();
        try {
            var issueField = PaypalApiException.PaypalErrorDetail.class.getDeclaredField("issue");
            var descField = PaypalApiException.PaypalErrorDetail.class.getDeclaredField("description");
            issueField.setAccessible(true);
            descField.setAccessible(true);
            issueField.set(detail, "INVALID_CARD");
            descField.set(detail, "inválido");
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
            fail("Falha no reflection");
        }

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
        assertEquals(0, ex.getStatusCode()); 
        assertNull(ex.getName());
        assertNull(ex.getDetails());
    }
}
