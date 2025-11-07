package dev.unirio.externalservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.unirio.externalservice.enumeration.StatusCobranca;

@ExtendWith(MockitoExtension.class)
class CobrancaDTOTest {

    @Test
    @DisplayName("Deve criar uma Cobranca DTO e validar as propriedades")
    void testCobrancaDTO() {
        Long id = 1L;
        BigDecimal valor = new BigDecimal("150.00");
        Long ciclista = 2L;
        StatusCobranca status = StatusCobranca.PENDENTE;
        LocalDateTime horaSolicitacao = LocalDateTime.now();
        LocalDateTime horaFinalizacao = LocalDateTime.now();

        CobrancaDTO dto = new CobrancaDTO(id, valor, ciclista, status, horaSolicitacao, horaFinalizacao);

        assertNotNull(dto);
        assertEquals(id, dto.getId());
        assertEquals(valor, dto.getValor());
        assertEquals(ciclista, dto.getCiclista());
        assertEquals(status, dto.getStatus());
        assertEquals(horaSolicitacao, dto.getHoraSolicitacao());
        assertEquals(horaFinalizacao, dto.getHoraFinalizacao());
    }
}
