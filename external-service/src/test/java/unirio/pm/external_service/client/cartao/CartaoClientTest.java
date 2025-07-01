package unirio.pm.external_service.client.cartao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import unirio.pm.external_service.dto.CartaoDTO;

@ExtendWith(MockitoExtension.class)
public class CartaoClientTest {

    private final CartaoClient client = new CartaoClient();

    @Test
    @DisplayName("Deve retornar um cartão com dados fixos")
    public void testBuscarCartao() {

        CartaoDTO dto = client.buscarCartao(1L);

        assertEquals("Titular", dto.getTitular());
        assertEquals("4002356147465716", dto.getNumero());
        assertEquals("2010-06", dto.getValidade());
        assertEquals("584", dto.getCvv());
    }

    @Test
    @DisplayName("Deve retornar um cartão com dados fixos")
    public void testBuscarCartaoCerto() {

        CartaoDTO dto = client.buscarCartaoCerto(1L);

        assertEquals("Titular", dto.getTitular());
        assertEquals("4002356147465716", dto.getNumero());
        assertEquals("2030-06", dto.getValidade());
        assertEquals("584", dto.getCvv());
    }
}
