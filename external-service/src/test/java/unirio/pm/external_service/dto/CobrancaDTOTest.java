package unirio.pm.external_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import unirio.pm.external_service.enumerations.StatusCobranca;

@SpringBootTest
public class CobrancaDTOTest {

    @Test
    @DisplayName("Should create CobrancaDTO and validate its properties")
    void testCobrancaDTO() {
        CobrancaDTO cobrancaDTO = new CobrancaDTO(new BigDecimal("100.0"), 1L);

        assertNotNull(cobrancaDTO);
        assertEquals(new BigDecimal("100.0"), cobrancaDTO.getValor());
        assertEquals(1L, cobrancaDTO.getCiclista());
        assertEquals(StatusCobranca.PENDENTE, cobrancaDTO.getStatus());
        assertEquals(LocalDate.now(), cobrancaDTO.getHoraSolicitacao());
    }
}