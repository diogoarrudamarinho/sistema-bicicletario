package dev.unirio.rentalservice.mapper;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.unirio.rentalservice.dto.CartaoDTO;
import dev.unirio.rentalservice.entity.Cartao;

@ExtendWith(MockitoExtension.class)
class CartaoMapperTest {
    private final CartaoMapperImpl mapper = new CartaoMapperImpl();

    private Cartao entity;
    private CartaoDTO dto;

    @BeforeEach
    void setUp() {
        entity = new Cartao();
        entity.setId(1L);
        entity.setNomeTitular("Diogo Arruda");
        entity.setNumero("123456789");
        entity.setValidade(LocalDate.of(2030, 1, 1));
        entity.setCvv("123");

        dto = new CartaoDTO(1L, "Diogo Arruda", "123456789", LocalDate.of(2030, 1, 1), "123");
    }

    // --- TESTES DA FUNÇÃO: toDto ---

    @Test
    void toDto() {
        CartaoDTO result = mapper.toDto(entity);

        assertNotNull(result);
        assertEquals(entity.getId(), result.id());
        assertEquals(entity.getNomeTitular(), result.nomeTitular());
        assertEquals(entity.getNumero(), result.numero());
        assertEquals(entity.getValidade(), result.validade());
        assertEquals(entity.getCvv(), result.cvv());
    }

    @Test
    void toDtoNull() {
        assertNull(mapper.toDto(null));
    }

    // --- TESTES DA FUNÇÃO: toEntity ---

    @Test
    void toEntity() {
        Cartao result = mapper.toEntity(dto);

        assertNotNull(result);
        assertEquals(dto.id(), result.getId());
        assertEquals(dto.nomeTitular(), result.getNomeTitular());
        assertEquals(dto.numero(), result.getNumero());
        assertEquals(dto.validade(), result.getValidade());
        assertEquals(dto.cvv(), result.getCvv());
    }

    @Test
    void toEntityNull() {
        assertNull(mapper.toEntity(null));
    }

    // --- TESTES DA FUNÇÃO: updateEntityFromDto ---

    @Test
    void updateEntityFromDto() {
        Cartao targetEntity = new Cartao();
        
        mapper.updateEntityFromDto(dto, targetEntity);

        assertEquals(dto.id(), targetEntity.getId());
        assertEquals(dto.nomeTitular(), targetEntity.getNomeTitular());
        assertEquals(dto.numero(), targetEntity.getNumero());
        assertEquals(dto.validade(), targetEntity.getValidade());
        assertEquals(dto.cvv(), targetEntity.getCvv());
    }

    @Test
    void updateEntityFromDtoNull() {
        Cartao targetEntity = new Cartao();
        targetEntity.setNomeTitular("Original");

        mapper.updateEntityFromDto(null, targetEntity);

        assertEquals("Original", targetEntity.getNomeTitular());
    }
}
