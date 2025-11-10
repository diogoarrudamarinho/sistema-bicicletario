package dev.unirio.externalservice.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.unirio.externalservice.enumeration.StatusCobranca;

@ExtendWith(MockitoExtension.class)
class CobrancaTest {
    
    @Test
    @DisplayName("Deve criar uma cobranca e validar suas propriedades")

    void testCobranca() {
        BigDecimal valor = new BigDecimal("100.0");
        Long ciclista = 1L;

        LocalDateTime before = LocalDateTime.now();
        Cobranca cobranca = new Cobranca(valor, ciclista);
        Cobranca cobranca2 = new Cobranca();
        LocalDateTime after = LocalDateTime.now();

        assertNotNull(cobranca);
        assertNull(cobranca.getId());
        assertNull(cobranca.getHoraFinalizacao());

        assertNotNull(cobranca2);
        assertNull(cobranca2.getId());
        assertNull(cobranca2.getHoraFinalizacao());

        assertEquals(StatusCobranca.PENDENTE, cobranca.getStatus());
        assertEquals(valor, cobranca.getValor());
        assertEquals(ciclista, cobranca.getCiclista());

        assertEquals(StatusCobranca.PENDENTE, cobranca2.getStatus());

        assertTrue(
        (cobranca.getHoraSolicitacao().isEqual(before) || cobranca.getHoraSolicitacao().isAfter(before)) &&
        (cobranca.getHoraSolicitacao().isEqual(after) || cobranca.getHoraSolicitacao().isBefore(after))
        );    

        assertTrue(
        (cobranca2.getHoraSolicitacao().isEqual(before) || cobranca2.getHoraSolicitacao().isAfter(before)) &&
        (cobranca2.getHoraSolicitacao().isEqual(after) || cobranca2.getHoraSolicitacao().isBefore(after))
        );
    }

    @Test
    @DisplayName("Deve buscar e atualizar os dados corretos do objeto")
    void testSettersAndGetters() {
        
        LocalDateTime before = LocalDateTime.now();
        Cobranca cobranca = new Cobranca(new BigDecimal("100.0"), 1l);

        cobranca.setId(10L);
        cobranca.setValor(new BigDecimal("200.0"));
        cobranca.setCiclista(2L);
        cobranca.setStatus(StatusCobranca.PAGA);
        cobranca.setHoraSolicitacao(LocalDateTime.now());
        cobranca.setHoraFinalizacao(LocalDateTime.now());

        LocalDateTime after = LocalDateTime.now();

        assertEquals(10L, cobranca.getId());
        assertEquals(new BigDecimal("200.0"), cobranca.getValor());
        assertEquals(2L, cobranca.getCiclista());
        assertEquals(StatusCobranca.PAGA, cobranca.getStatus());
         assertTrue(
        (cobranca.getHoraSolicitacao().isEqual(before) || cobranca.getHoraSolicitacao().isAfter(before)) &&
        (cobranca.getHoraSolicitacao().isEqual(after) || cobranca.getHoraSolicitacao().isBefore(after))
        );
         assertTrue(
        (cobranca.getHoraFinalizacao().isEqual(before) || cobranca.getHoraFinalizacao().isAfter(before)) &&
        (cobranca.getHoraFinalizacao().isEqual(after) || cobranca.getHoraFinalizacao().isBefore(after))
        );      
       
    }

    @Test
    @DisplayName("Deve validar o Hashcode e Equals")
    void testHashCodeAndEquals() {
        Cobranca cobranca1 = new Cobranca(new BigDecimal("100.0"), 1L);
        cobranca1.setId(1L);

        Cobranca cobranca2 = new Cobranca(new BigDecimal("200.0"), 2L);
        cobranca2.setId(1L);

        Cobranca cobranca3 = new Cobranca(new BigDecimal("300.0"), 3L);
        cobranca3.setId(2L);

        Cobranca cobranca4 = new Cobranca(new BigDecimal("400.0"), 4L);
        cobranca4.setId(null);

        Cobranca cobranca5 = null;

        List<Integer> list = new ArrayList<>();

        assertEquals(cobranca1, cobranca1);
        assertEquals(cobranca1, cobranca2);
        assertNotEquals(null, cobranca1); 
        assertNotEquals(cobranca1, cobranca3); 
        assertNotEquals(cobranca1, list);
        assertNotEquals(cobranca4, cobranca1);
        assertNotEquals(cobranca1, cobranca5);

        assertEquals(cobranca1.hashCode(), cobranca2.hashCode()); 
        assertNotEquals(cobranca1.hashCode(), cobranca3.hashCode());
    }
}
