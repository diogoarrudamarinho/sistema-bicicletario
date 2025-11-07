package dev.unirio.externalservice.mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dev.unirio.externalservice.dto.CobrancaDTO;
import dev.unirio.externalservice.entity.Cobranca;
import dev.unirio.externalservice.enumeration.StatusCobranca;

class CobrancaMapperTest {

    private CobrancaMapper mapper;
    private Cobranca entity;
    private CobrancaDTO dto;

    @BeforeEach
    @SuppressWarnings("unused")
    void setup() {
        mapper = new CobrancaMapper();

        entity = new Cobranca();
        entity.setId(1L);
        entity.setValor(new BigDecimal("50.00"));
        entity.setCiclista(123L);
        entity.setStatus(StatusCobranca.PAGA);
        entity.setHoraSolicitacao(LocalDateTime.of(2025, 7, 1, 10, 0));
        entity.setHoraFinalizacao(LocalDateTime.of(2025, 7, 1, 10, 10));

        dto = new CobrancaDTO(
                1L,
                new BigDecimal("50.00"),
                123L,
                StatusCobranca.PAGA,
                LocalDateTime.of(2025, 7, 1, 10, 0),
                LocalDateTime.of(2025, 7, 1, 10, 10)
        );
    }

    @Test
    @DisplayName("Deve converter corretamente de Entity para DTO")
    void testToDTO() {
        CobrancaDTO result = mapper.toDTO(entity);

        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getValor(), result.getValor());
        assertEquals(entity.getCiclista(), result.getCiclista());
        assertEquals(entity.getStatus(), result.getStatus());
        assertEquals(entity.getHoraSolicitacao(), result.getHoraSolicitacao());
        assertEquals(entity.getHoraFinalizacao(), result.getHoraFinalizacao());
    }

    @Test
    @DisplayName("Deve converter corretamente de DTO para Entity")
    void testToEntity() {
        Cobranca result = mapper.toEntity(dto);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getValor(), result.getValor());
        assertEquals(dto.getCiclista(), result.getCiclista());
        assertEquals(dto.getStatus(), result.getStatus());
        assertEquals(dto.getHoraSolicitacao(), result.getHoraSolicitacao());
        assertEquals(dto.getHoraFinalizacao(), result.getHoraFinalizacao());
    }

    @Test
    @DisplayName("toDTO deve retornar null se input for null")
    void testToDTONull() {
        assertNull(mapper.toDTO(null));
    }

    @Test
    @DisplayName("toEntity deve retornar null se input for null")
    void testToEntityNull() {
        assertNull(mapper.toEntity(null));
    }
}
