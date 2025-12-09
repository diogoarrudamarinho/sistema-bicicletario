package dev.unirio.equipmentservice.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.unirio.equipmentservice.enumeration.BicicletaStatus;

class BicicletaRequestDTOTest {

    private BicicletaRequestDTO dto; 
    
    // Dados válidos para inicialização
    private static final String MARCA = "Caloi";
    private static final String MODELO = "Elite";
    private static final String ANO = "2023";
    private static final Integer NUMERO = 101;
    private static final BicicletaStatus STATUS = BicicletaStatus.DISPONIVEL;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        dto = new BicicletaRequestDTO(MARCA, MODELO, 
                                       ANO, NUMERO, 
                                       STATUS);
    }
    
    @Test
    void testConstrutorCompletoEGetters() {
        assertEquals(MARCA, dto.getMarca());
        assertEquals(MODELO, dto.getModelo());
        assertEquals(ANO, dto.getAno());
        assertEquals(NUMERO, dto.getNumero());
        assertEquals(STATUS, dto.getStatus());
    }

    @Test
    void testConstrutorVazio() {
        BicicletaRequestDTO novoDto = new BicicletaRequestDTO();
        assertNotNull(novoDto);
        assertNull(novoDto.getMarca());
    }
    
    @Test
    void testSettersEGetters() {
        String novaMarca = "Giant";
        String novoModelo = "Modelo legal";
        Integer novoNumero = 200;
        BicicletaStatus novoStatus = BicicletaStatus.EM_USO;

        dto.setMarca(novaMarca);
        dto.setNumero(novoNumero);
        dto.setStatus(novoStatus);
        dto.setModelo(novoModelo);

        assertEquals(novaMarca, dto.getMarca());
        assertEquals(novoNumero, dto.getNumero());
        assertEquals(novoStatus, dto.getStatus());
    }

    @Test
    void testSettersAceitamValoresNulosOuVazios() {
        dto.setMarca(null);
        dto.setAno("");
        dto.setNumero(0); 

        assertNull(dto.getMarca());
        assertEquals("", dto.getAno());
        assertEquals(0, dto.getNumero());
    }
}