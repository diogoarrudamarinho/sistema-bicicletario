package dev.unirio.externalservice.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CartaoDTOTest {

    @Test
    @DisplayName("Should create CartaoDTO and validate its properties")
    void testCartaoDTO() {
        CartaoDTO cartaoDTO = new CartaoDTO();
        cartaoDTO.setTitular("Test Name");
        cartaoDTO.setNumero("1234567890123456");
        cartaoDTO.setValidade("2025-12");
        cartaoDTO.setCvv("123");

        assertNotNull(cartaoDTO);
        assertEquals("1234567890123456", cartaoDTO.getNumero());
        assertEquals("Test Name", cartaoDTO.getTitular());
        assertEquals("2025-12", cartaoDTO.getValidade());
        assertEquals("123", cartaoDTO.getCvv());

        cartaoDTO = new CartaoDTO("Test Name", "1234567890123456", "2025-12", "123");

        assertNotNull(cartaoDTO);
        assertEquals("1234567890123456", cartaoDTO.getNumero());
        assertEquals("Test Name", cartaoDTO.getTitular());
        assertEquals("2025-12", cartaoDTO.getValidade());
        assertEquals("123", cartaoDTO.getCvv());
    }
}