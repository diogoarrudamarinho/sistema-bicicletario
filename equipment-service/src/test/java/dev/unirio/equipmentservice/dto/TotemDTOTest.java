package dev.unirio.equipmentservice.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class TotemDTOTest {
    @Test
    void testTotemDTOGettersAndSetters() {
        TotemDTO dto = new TotemDTO();
        dto.setId(1L);
        dto.setLocalizacao("Rua A");
        dto.setDescricao("Perto do metrô");

        assertEquals(1L, dto.getId());
        assertEquals("Rua A", dto.getLocalizacao());
        assertEquals("Perto do metrô", dto.getDescricao());
    }

    @Test
    void testTotemDTOConstructor() {
        TotemDTO dto = new TotemDTO(1L, "Rua B", "Lado sul");
        assertEquals(1L, dto.getId());
        assertEquals("Rua B", dto.getLocalizacao());
    }

    @Test
    void testTotemRequestDTOGettersAndSetters() {
        TotemRequestDTO dto = new TotemRequestDTO();
        dto.setLocalizacao("Rua C");
        dto.setDescricao("Nova unidade");

        assertEquals("Rua C", dto.getLocalizacao());
        assertEquals("Nova unidade", dto.getDescricao());
    }

    @Test
    void testTotemRequestDTO_ConstructorWithArgs() {
        String descricao = "Totem Central";
        String localizacao = "Largo do Machado";
        
        TotemRequestDTO dto = new TotemRequestDTO(localizacao, descricao);
        
        assertEquals(descricao, dto.getDescricao());
        assertEquals(localizacao, dto.getLocalizacao());
    }
}
