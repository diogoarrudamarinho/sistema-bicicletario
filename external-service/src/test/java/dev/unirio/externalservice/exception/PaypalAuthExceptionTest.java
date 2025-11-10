package dev.unirio.externalservice.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.unirio.externalservice.exception.cobranca.PaypalAuthException;

@ExtendWith(MockitoExtension.class)
class PaypalAuthExceptionTest {

    @Test
    void testConstrutor() {
        String msg = "Erro";
        PaypalAuthException ex = new PaypalAuthException(msg);
        
        assertEquals(msg, ex.getMessage());
    }
}
