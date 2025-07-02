package unirio.pm.external_service.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import unirio.pm.external_service.exception.cobranca.PaypalAuthException;

@ExtendWith(MockitoExtension.class)
public class PaypalAuthExceptionTest {

    @Test
    void testConstrutor() {
        String msg = "Erro";
        PaypalAuthException ex = new PaypalAuthException(msg);
        
        assertEquals(msg, ex.getMessage());
    }
}
