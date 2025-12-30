package dev.unirio.equipmentservice.mapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.unirio.equipmentservice.dto.TrancaDTO;
import dev.unirio.equipmentservice.dto.TrancaRequestDTO;
import dev.unirio.equipmentservice.entity.Bicicleta;
import dev.unirio.equipmentservice.entity.Tranca;
import dev.unirio.equipmentservice.enumeration.TrancaStatus;

class TrancaMapperTest {

    private TrancaMapper mapper;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        mapper = new TrancaMapper();
    }

    @Test
    void toDto_DeveMapearComBicicleta() {
        // Mock da Bicicleta
        Bicicleta bike = new Bicicleta();
        bike.setId(50L);

        Tranca tranca = new Tranca();
        tranca.setId(1L);
        tranca.setNumero(10);
        tranca.setBicicleta(bike);
        tranca.setStatus(TrancaStatus.OCUPADA);

        TrancaDTO dto = mapper.toDto(tranca);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(50L, dto.getBicicleta()); 
        assertEquals(TrancaStatus.OCUPADA, dto.getStatus());
    }

    @Test
    void toDto_SemBicicleta_DeveMapearBicicletaComoNull() {
        Tranca tranca = new Tranca();
        tranca.setBicicleta(null);

        TrancaDTO dto = mapper.toDto(tranca);

        assertNull(dto.getBicicleta());
    }

    @Test
    void toEntity_DeveMapearRequest() {
        TrancaRequestDTO request = new TrancaRequestDTO(1, "Rua B", "2024", "X", TrancaStatus.LIVRE);

        Tranca tranca = mapper.toEntity(request);

        assertNotNull(tranca);
        assertEquals(1, tranca.getNumero());
        assertEquals("X", tranca.getModelo());
        assertEquals(TrancaStatus.LIVRE, tranca.getStatus());
    }

    @Test
    void updateEntityFromDto_DeveManterIntegridade() {
        Tranca tranca = new Tranca();
        tranca.setId(1L); 
        
        TrancaRequestDTO request = new TrancaRequestDTO(2, "Local", "2025", "Y", TrancaStatus.EM_REPARO);

        mapper.updateEntityFromDto(request, tranca);

        assertEquals(2, tranca.getNumero());
        assertEquals(TrancaStatus.EM_REPARO, tranca.getStatus());
        assertEquals(1L, tranca.getId()); 
    }

    @Test
    void toDtoList_ComDados_DeveMapearTodos() {
        List<Tranca> lista = List.of(new Tranca(), new Tranca());
        assertEquals(2, mapper.toDtoList(lista).size());
    }

    @Test
    void toNull(){
        assertNull(mapper.toDto(null));
        assertTrue(mapper.toDtoList(null).isEmpty());
        assertNull(mapper.toEntity(null));
        mapper.updateEntityFromDto(null, null);
    }
}