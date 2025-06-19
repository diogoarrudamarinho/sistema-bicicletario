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
    public void testCobrancaDTO() {
        Long id = 1L;
        BigDecimal valor = new BigDecimal("150.00");
        Long ciclista = 2L;
        StatusCobranca status = StatusCobranca.PENDENTE;
        LocalDate horaSolicitacao = LocalDate.of(2023, 10, 1);
        LocalDate horaFinalizacao = LocalDate.of(2023, 10, 2);

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
