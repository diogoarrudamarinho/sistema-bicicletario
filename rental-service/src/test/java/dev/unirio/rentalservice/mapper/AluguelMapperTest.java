package dev.unirio.rentalservice.mapper;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.unirio.rentalservice.dto.AluguelDTO;
import dev.unirio.rentalservice.entity.Aluguel;

@ExtendWith(MockitoExtension.class)
class AluguelMapperTest {

    private final AluguelMapperImpl mapper = new AluguelMapperImpl();

    private Aluguel entity;
    private AluguelDTO dto;
    private LocalDateTime agora;

    @BeforeEach
    void setUp() {
        agora = LocalDateTime.now();
        
        entity = new Aluguel();
        entity.setId(100L);
        entity.setCiclista(1L);
        entity.setBicicleta(10L);
        entity.setTrancaInicio(5L);
        entity.setTrancaFim(6L);
        entity.setHoraInicio(agora);
        entity.setHoraFim(agora.plusHours(1));
        entity.setCobranca(500L);

        dto = new AluguelDTO(100L, 1L, 10L, 5L, 6L, agora, agora.plusHours(1), 500L);
    }

    // --- TESTES DA FUNÇÃO: toDto ---

    @Test
    void toDto() {
        AluguelDTO result = mapper.toDto(entity);

        assertNotNull(result);
        assertEquals(entity.getId(), result.id());
        assertEquals(entity.getCiclista(), result.ciclista());
        assertEquals(entity.getBicicleta(), result.bicicleta());
        assertEquals(entity.getTrancaInicio(), result.trancaInicio());
        assertEquals(entity.getTrancaFim(), result.trancaFim());
        assertEquals(entity.getHoraInicio(), result.horaInicio());
        assertEquals(entity.getHoraFim(), result.horaFim());
        assertEquals(entity.getCobranca(), result.cobranca());
    }

    @Test
    void toDtoNull() {
        assertNull(mapper.toDto(null));
    }

    // --- TESTES DA FUNÇÃO: toEntity ---

    @Test
    void toEntity() {
        Aluguel result = mapper.toEntity(dto);

        assertNotNull(result);
        assertEquals(dto.id(), result.getId());
        assertEquals(dto.ciclista(), result.getCiclista());
        assertEquals(dto.bicicleta(), result.getBicicleta());
        assertEquals(dto.cobranca(), result.getCobranca());
        assertEquals(dto.trancaInicio(), result.getTrancaInicio());
        assertEquals(dto.trancaFim(), result.getTrancaFim());
        assertEquals(dto.horaInicio(), result.getHoraInicio());
        assertEquals(dto.horaFim(), result.getHoraFim());
    }

    @Test
    void toEntityNull() {
        assertNull(mapper.toEntity(null));
    }
}
