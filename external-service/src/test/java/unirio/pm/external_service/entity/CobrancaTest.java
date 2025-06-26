package unirio.pm.external_service.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import unirio.pm.external_service.enumerations.StatusCobranca;

@SpringBootTest
public class CobrancaTest {
    
    @Test
    @DisplayName("Should create Cobranca and validate its properties")
    public void testCobranca() {
        BigDecimal valor = new BigDecimal("100.0");
        Long ciclista = 1L;

        Cobranca cobranca = new Cobranca(valor, ciclista);

        assertNotNull(cobranca);
        assertEquals(valor, cobranca.getValor());
        assertEquals(ciclista, cobranca.getCiclista());
        assertEquals(StatusCobranca.PENDENTE, cobranca.getStatus());
        assertEquals(LocalDateTime.now(), cobranca.getHoraSolicitacao());
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    public void testSettersAndGetters() {
        Cobranca cobranca = new Cobranca(new BigDecimal("100.0"), 1L);

        cobranca.setId(10L);
        cobranca.setValor(new BigDecimal("200.0"));
        cobranca.setCiclista(2L);
        cobranca.setStatus(StatusCobranca.PAGA);
        cobranca.setHoraSolicitacao(LocalDateTime.now());
        cobranca.setHoraFinalizacao(LocalDateTime.now());

        assertEquals(10L, cobranca.getId());
        assertEquals(new BigDecimal("200.0"), cobranca.getValor());
        assertEquals(2L, cobranca.getCiclista());
        assertEquals(StatusCobranca.PAGA, cobranca.getStatus());
        assertEquals(LocalDateTime.now(), cobranca.getHoraSolicitacao());
        assertEquals(LocalDateTime.now(), cobranca.getHoraFinalizacao());
    }

    @Test
    @DisplayName("Should validate hashCode and equals methods for Cobranca")
    public void testHashCodeAndEquals() {
        Cobranca cobranca1 = new Cobranca(new BigDecimal("100.0"), 1L);
        cobranca1.setId(1L);

        Cobranca cobranca2 = new Cobranca(new BigDecimal("200.0"), 2L);
        cobranca2.setId(1L);

        Cobranca cobranca3 = new Cobranca(new BigDecimal("300.0"), 3L);
        cobranca3.setId(2L);

        assertEquals(cobranca1, cobranca2); 
        assertNotEquals(cobranca1, cobranca3); 

        assertEquals(cobranca1.hashCode(), cobranca2.hashCode()); 
        assertNotEquals(cobranca1.hashCode(), cobranca3.hashCode());
    }
}
