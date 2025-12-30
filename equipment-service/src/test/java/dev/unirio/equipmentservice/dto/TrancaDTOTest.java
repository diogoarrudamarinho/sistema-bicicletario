package dev.unirio.equipmentservice.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

import dev.unirio.equipmentservice.enumeration.TrancaStatus;

class TrancaDTOTest {
    
    @Test
    void testTrancaDTO_ConstructorWithArgs() {
        Long id = 1L;
        Long bikeId = 10L;
        Integer numero = 500;
        String local = "Praia de Botafogo";
        String ano = "2023";
        String modelo = "T-1000";
        TrancaStatus status = TrancaStatus.LIVRE;

        TrancaDTO dto = new TrancaDTO(id, bikeId, numero, local, ano, modelo, status);

        assertEquals(id, dto.getId());
        assertEquals(bikeId, dto.getBicicleta());
        assertEquals(numero, dto.getNumero());
        assertEquals(local, dto.getLocalizacao());
        assertEquals(ano, dto.getAnoDeFabricacao());
        assertEquals(modelo, dto.getModelo());
        assertEquals(status, dto.getStatus());
    }

    @Test
    void testTrancaDTOGettersAndSetters() {
        TrancaDTO dto = new TrancaDTO();
        dto.setId(10L);
        dto.setBicicleta(5L);
        dto.setNumero(101);
        dto.setStatus(TrancaStatus.LIVRE);

        assertEquals(10L, dto.getId());
        assertEquals(5L, dto.getBicicleta());
        assertEquals(101, dto.getNumero());
        assertEquals(TrancaStatus.LIVRE, dto.getStatus());
    }

    @Test
    void testTrancaRequestDTOConstructorAndValidation() {
        TrancaRequestDTO request = new TrancaRequestDTO(50, "Rua X", "2024", "Mod 1", TrancaStatus.OCUPADA);
        
        assertEquals("2024", request.getAnoDeFabricacao());
        assertEquals(50, request.getNumero());
        assertEquals(TrancaStatus.OCUPADA, request.getStatus());
    }

    @Test
    void testTrancaRequestDTO_EmptyConstructor() {
        TrancaRequestDTO dto = new TrancaRequestDTO();
        
        assertNotNull(dto);
        assertNull(dto.getNumero());
        assertNull(dto.getModelo());
        
        dto.setNumero(15);
        assertEquals(15, dto.getNumero());
    }
}
