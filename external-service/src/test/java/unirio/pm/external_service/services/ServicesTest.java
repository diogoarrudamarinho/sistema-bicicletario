package unirio.pm.external_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import unirio.pm.external_service.dto.CartaoDTO;
import unirio.pm.external_service.dto.CobrancaDTO;
import unirio.pm.external_service.dto.EmailDTO;

@SpringBootTest
public class ServicesTest {
   
    @Autowired
    private Services services;
    
    @Test
    @DisplayName("Should return 'Hello world' when is called")
    void testHelloWorld() {
        String resultado = services.helloWorld();
        assertEquals("Hello world", resultado);
    }

    @Test
    void testRestaurarBanco() {
        UnsupportedOperationException exception = assertThrows(
            UnsupportedOperationException.class,
            () -> services.restaurarBanco()
        );
        assertEquals("Not supported yet.", exception.getMessage());
    }

    @Test
    void testEnviarEmail() {
        UnsupportedOperationException exception = assertThrows(
            UnsupportedOperationException.class,
            () -> services.enviarEmail(new EmailDTO())
        );
        assertEquals("Not supported yet.", exception.getMessage());
    }

    @Test
    void testCriarCobranca() {
        UnsupportedOperationException exception = assertThrows(
            UnsupportedOperationException.class,
            () -> services.criarCobranca(new CobrancaDTO())
        );
        assertEquals("Not supported yet.", exception.getMessage());
    }

    @Test
    void testValidarCatao() {
        UnsupportedOperationException exception = assertThrows(
            UnsupportedOperationException.class,
            () -> services.validarCartao(new CartaoDTO())
        );
        assertEquals("Not supported yet.", exception.getMessage());
    }
}
