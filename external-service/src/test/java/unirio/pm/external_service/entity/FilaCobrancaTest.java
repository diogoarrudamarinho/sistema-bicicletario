package unirio.pm.external_service.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import unirio.pm.external_service.enumerations.StatusCobranca;

@ExtendWith(MockitoExtension.class)
class FilaCobrancaTest {

    @Test
    void testConstrutor() {
        BigDecimal valor = new BigDecimal("50.00");
        Long ciclistaId = 1L;

        FilaCobranca fila = new FilaCobranca(valor, ciclistaId);

        assertEquals(valor, fila.getValor());
        assertEquals(ciclistaId, fila.getCiclista());
        assertEquals(StatusCobranca.PENDENTE, fila.getStatus());
        assertNotNull(fila.getHoraSolicitacao());
        assertNull(fila.getHoraFinalizacao());
    }

    @Test
    void testSettersAndGetters() {
        FilaCobranca fila = new FilaCobranca();

        LocalDateTime agora = LocalDateTime.now();
        fila.setId(100L);
        fila.setValor(new BigDecimal("75.00"));
        fila.setCiclista(2L);
        fila.setStatus(StatusCobranca.PAGA);
        fila.setHoraSolicitacao(agora);
        fila.setHoraFinalizacao(agora.plusHours(1));

        assertEquals(100L, fila.getId());
        assertEquals(new BigDecimal("75.00"), fila.getValor());
        assertEquals(2L, fila.getCiclista());
        assertEquals(StatusCobranca.PAGA, fila.getStatus());
        assertEquals(agora, fila.getHoraSolicitacao());
        assertEquals(agora.plusHours(1), fila.getHoraFinalizacao());
    }

    @Test
    void testEqualsAndHashCode() {
        FilaCobranca f1 = new FilaCobranca();
        f1.setId(10L);

        FilaCobranca f2 = new FilaCobranca();
        f2.setId(10L);

        FilaCobranca f3 = new FilaCobranca();
        f3.setId(20L);

        List<Integer> list = new ArrayList<>();

        assertEquals(f1, f1);
        assertEquals(f1, f2);
        
        assertNotEquals(null, f1);
        assertNotEquals(f1, list);
        assertNotEquals(f1, f3);

        assertEquals(f1.hashCode(), f2.hashCode());
        assertNotEquals(f1.hashCode(), f3.hashCode());
    }

    @Test
    void testEqualsComNullId() {
        FilaCobranca f1 = new FilaCobranca();
        FilaCobranca f2 = new FilaCobranca();

        assertEquals(f1, f2);
        assertEquals(f1.hashCode(), f2.hashCode());
    }
}
