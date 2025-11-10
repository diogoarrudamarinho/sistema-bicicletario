package dev.unirio.externalservice.client.cartao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.unirio.externalservice.client.paypal.cartao.CartaoClient;
import dev.unirio.externalservice.dto.CartaoDTO;

@ExtendWith(MockitoExtension.class)
class CartaoClientTest {
    
    @Test
    @DisplayName("O construtor vazio deve instanciar a classe sem lançar exceções")
    void testConstructorVazio() {
        
        CartaoClient client = assertDoesNotThrow(CartaoClient::new);
        
        assertNotNull(client);
    }

    @Test
    @DisplayName("Deve retornar o cartao corretamente")
    void testBuscaCartao() {
        
        CartaoClient client = new CartaoClient();
        Long testId = 1L; 

        CartaoDTO result = client.buscaCartao(testId);

        assertNotNull(result);
        assertEquals("Diogo", result.getTitular());
        assertEquals("4032039366042275", result.getNumero());
        assertEquals("08/2027", result.getValidade());
        assertEquals("798", result.getCvv());
    }
}
