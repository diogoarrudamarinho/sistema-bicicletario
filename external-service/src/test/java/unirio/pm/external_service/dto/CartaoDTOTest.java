package unirio.pm.external_service.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CartaoDTOTest {

    @Test
    @DisplayName("Should create CartaoDTO and validate its properties")
    public void testCartaoDTO() {
        CartaoDTO cartaoDTO = new CartaoDTO();
        cartaoDTO.setTitular("Test Name");
        cartaoDTO.setNumero("1234567890123456");
        cartaoDTO.setValidade("12/25");
        cartaoDTO.setCvv("123");

        assertNotNull(cartaoDTO);
        assertEquals("1234567890123456", cartaoDTO.getNumero());
        assertEquals("Test Name", cartaoDTO.getTitular());
        assertEquals("12/25", cartaoDTO.getValidade());
        assertEquals("123", cartaoDTO.getCvv());
    }
}