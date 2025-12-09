package dev.unirio.equipmentservice.mapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.unirio.equipmentservice.dto.BicicletaDTO;
import dev.unirio.equipmentservice.dto.BicicletaRequestDTO;
import dev.unirio.equipmentservice.entity.Bicicleta;
import dev.unirio.equipmentservice.enumeration.BicicletaStatus;

class BicicletaMapperTest {

    private BicicletaMapper mapper;
    
    private static final Long ID = 1L;
    private static final String MARCA = "Caloi";
    private static final String MODELO = "Elite";
    private static final Integer NUMERO = 101;
    private static final BicicletaStatus STATUS = BicicletaStatus.DISPONIVEL;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        mapper = new BicicletaMapper();
    }

    @Test
    void testToDto() {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(ID);
        bicicleta.setMarca(MARCA);
        bicicleta.setModelo(MODELO);
        bicicleta.setNumero(NUMERO);
        bicicleta.setStatus(STATUS);

        BicicletaDTO dto = mapper.toDto(bicicleta);

        assertNotNull(dto);
        assertEquals(ID, dto.getId());
        assertEquals(MARCA, dto.getMarca());
        assertEquals(STATUS, dto.getStatus());
    }

    @Test
    void testToDtoNull() {
        assertNull(mapper.toDto(null));
    }

    @Test
    void testToDtoList() {
        Bicicleta b1 = new Bicicleta();
        b1.setId(1L);
        b1.setMarca("A");
        Bicicleta b2 = new Bicicleta();
        b2.setId(2L);
        b2.setMarca("B");
        
        List<Bicicleta> bicicletas = Arrays.asList(b1, b2);

        List<BicicletaDTO> dtoList = mapper.toDtoList(bicicletas);

        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals("A", dtoList.get(0).getMarca());
        assertEquals(2L, dtoList.get(1).getId());
    }

    @Test
    void testToDtoListNull() {
        List<BicicletaDTO> dtoList = mapper.toDtoList(null);

        assertNotNull(dtoList);
        assertTrue(dtoList.isEmpty());
    }
    
    @Test
    void testToDtoListEmpty() {
        List<BicicletaDTO> dtoList = mapper.toDtoList(Collections.emptyList());

        assertNotNull(dtoList);
        assertTrue(dtoList.isEmpty());
    }

    @Test
    void testToEntity() {
        BicicletaRequestDTO requestDTO = new BicicletaRequestDTO(MARCA, MODELO, "2024", NUMERO, STATUS);
        Bicicleta bicicleta = mapper.toEntity(requestDTO);

        assertNotNull(bicicleta);
        assertEquals(MARCA, bicicleta.getMarca());
        assertEquals(STATUS, bicicleta.getStatus());
        assertNull(bicicleta.getId()); 
    }

    @Test
    void testToEntityNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void testUpdateEntityFromDto() {
        Bicicleta bicicletaExistente = new Bicicleta();
        bicicletaExistente.setId(99L); 
        bicicletaExistente.setMarca("MarcaAntiga");
        bicicletaExistente.setStatus(BicicletaStatus.REPARO_SOLICITADO);

        BicicletaRequestDTO requestDTO = new BicicletaRequestDTO("MarcaNova", "ModeloNovo", "2025", 200, BicicletaStatus.EM_USO);

        mapper.updateEntityFromDto(requestDTO, bicicletaExistente);

        assertEquals("MarcaNova", bicicletaExistente.getMarca());
        assertEquals(BicicletaStatus.EM_USO, bicicletaExistente.getStatus());
        
        assertEquals(99L, bicicletaExistente.getId()); 
    }

    @Test
    void testUpdateEntityFromDtoRequestNull() {
        Bicicleta bicicletaExistente = new Bicicleta();
        bicicletaExistente.setId(99L);
        bicicletaExistente.setMarca("Original");

        mapper.updateEntityFromDto(null, bicicletaExistente);

        assertEquals("Original", bicicletaExistente.getMarca());
    }

     @Test
    void testUpdateEntityFromDtoBicicletaNull() {
        BicicletaRequestDTO bicicleta = new BicicletaRequestDTO();
        bicicleta.setMarca(MARCA);

        mapper.updateEntityFromDto(bicicleta, null);

        assertEquals(MARCA, bicicleta.getMarca());
    }
}