package dev.unirio.equipmentservice.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

import dev.unirio.equipmentservice.enumeration.BicicletaStatus;

class BicicletaDTOTest {

    private static final Long ID_TESTE = 1L;
    private static final String MARCA_TESTE = "Caloi";
    private static final String MODELO_TESTE = "Elite";
    private static final String ANO_TESTE = "2023";
    private static final Integer NUMERO_TESTE = 101;
    private static final BicicletaStatus STATUS_TESTE = BicicletaStatus.DISPONIVEL;

    // Teste para o construtor vazio
    @Test
    void testConstrutorVazio() {
        BicicletaDTO dto = new BicicletaDTO();
        assertNotNull(dto);
        assertNull(dto.getId());
    }

    // Teste para o construtor completo
    @Test
    void testConstrutorCompleto() {
        BicicletaDTO dto = new BicicletaDTO(ID_TESTE, MARCA_TESTE, MODELO_TESTE, ANO_TESTE, NUMERO_TESTE, STATUS_TESTE);

        assertEquals(ID_TESTE, dto.getId());
        assertEquals(MARCA_TESTE, dto.getMarca());
        assertEquals(MODELO_TESTE, dto.getModelo());
        assertEquals(ANO_TESTE, dto.getAno());
        assertEquals(NUMERO_TESTE, dto.getNumero());
        assertEquals(STATUS_TESTE, dto.getStatus());
    }

    // Teste para os Setters e Getters
    @Test
    void testSettersEGetters() {
        BicicletaDTO dto = new BicicletaDTO();

        dto.setId(ID_TESTE);
        dto.setMarca(MARCA_TESTE);
        dto.setModelo(MODELO_TESTE);
        dto.setAno(ANO_TESTE);
        dto.setNumero(NUMERO_TESTE);
        dto.setStatus(STATUS_TESTE);

        assertEquals(ID_TESTE, dto.getId());
        assertEquals(MARCA_TESTE, dto.getMarca());
        assertEquals(MODELO_TESTE, dto.getModelo());
        assertEquals(ANO_TESTE, dto.getAno());
        assertEquals(NUMERO_TESTE, dto.getNumero());
        assertEquals(STATUS_TESTE, dto.getStatus());
    }
}