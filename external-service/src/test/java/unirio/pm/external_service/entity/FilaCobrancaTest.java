package unirio.pm.external_service.entity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FilaCobrancaTest {
    
    @Test
    @DisplayName("Should create FilaCobranca and validate its properties")
    public void testFilaCobrancaCreation() {
        Long cobrancaId = 10L;
        FilaCobranca fila = new FilaCobranca(cobrancaId);

        assertNotNull(fila);
        assertEquals(cobrancaId, fila.getCobrancaId());
        // Por padrão, pago é false
        assertEquals(false, fila.isPago());
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    public void testSettersAndGetters() {
        FilaCobranca fila = new FilaCobranca();
        fila.setId(1L);
        fila.setCobrancaId(20L);
        fila.setPago(true);
        LocalDateTime data = LocalDateTime.of(2023, 10, 1, 12, 0);
        fila.setDataCobranca(data);

        assertEquals(1L, fila.getId());
        assertEquals(20L, fila.getCobrancaId());
        assertEquals(true, fila.isPago());
        assertEquals(data, fila.getDataCobranca());
    }

    @Test
    @DisplayName("Should validate hashCode and equals methods for FilaCobranca")
    public void testHashCodeAndEquals() {
        FilaCobranca fila1 = new FilaCobranca(100L);
        fila1.setId(1L);

        FilaCobranca fila2 = new FilaCobranca(200L);
        fila2.setId(1L);

        FilaCobranca fila3 = new FilaCobranca(300L);
        fila3.setId(2L);

        assertEquals(fila1, fila2);
        assertNotEquals(fila1, fila3);

        assertEquals(fila1.hashCode(), fila2.hashCode());
        assertNotEquals(fila1.hashCode(), fila3.hashCode());
    }
}
