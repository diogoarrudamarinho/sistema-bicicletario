package dev.unirio.equipmentservice.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import dev.unirio.equipmentservice.enumeration.TrancaStatus;

class TrancaIntegracaoDTOTest {
   
    @Test
    void testTrancaIntegracaoDTO_ConstructorAndGetters() {
        TrancaIntegracaoDTO dto = new TrancaIntegracaoDTO(100L, 200L, 300L, null);
        dto.setStatus(TrancaStatus.LIVRE);

        assertEquals(300L, dto.getFuncionario());
        assertEquals(200L, dto.getTotem());
        assertEquals(100L, dto.getTranca());
        assertEquals(TrancaStatus.LIVRE, dto.getStatus());
    }

    @Test
    void testTrancaIntegracaoDTO_EmptyConstructor() {
        TrancaIntegracaoDTO dto = new TrancaIntegracaoDTO();
        dto.setTranca(1L);
        assertNotNull(dto);
        assertEquals(1L, dto.getTranca());
    }
}
