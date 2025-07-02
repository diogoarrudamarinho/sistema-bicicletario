package unirio.pm.external_service.dto;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CobrancaRequestDTOTest {

    @Test
    @DisplayName("Should create CobrancaDTO and validate its properties")
    void testCobrancaDTO() {
        CobrancaRequestDTO cobrancaDTO = new CobrancaRequestDTO();
        cobrancaDTO.setCiclista(1L);
        cobrancaDTO.setValor(new BigDecimal("100.0"));

        assertNotNull(cobrancaDTO);
        assertEquals(new BigDecimal("100.0"), cobrancaDTO.getValor());
        assertEquals(1L, cobrancaDTO.getCiclista());
    }
}