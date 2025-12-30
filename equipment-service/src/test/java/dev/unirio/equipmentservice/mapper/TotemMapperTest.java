package dev.unirio.equipmentservice.mapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.unirio.equipmentservice.dto.TotemDTO;
import dev.unirio.equipmentservice.dto.TotemRequestDTO;
import dev.unirio.equipmentservice.entity.Totem;

class TotemMapperTest {

    private TotemMapper mapper;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        mapper = new TotemMapper();
    }

    @Test
    void toDto_DeveMapearCorretamente() {
        Totem totem = new Totem();
        totem.setId(1L);
        totem.setLocalizacao("Rua A");
        totem.setDescricao("Descricao A");

        TotemDTO dto = mapper.toDto(totem);

        assertNotNull(dto);
        assertEquals(totem.getId(), dto.getId());
        assertEquals(totem.getLocalizacao(), dto.getLocalizacao());
        assertEquals(totem.getDescricao(), dto.getDescricao());
    }

    @Test
    void toDtoList_DeveMapearLista() {
        Totem t1 = new Totem();
        t1.setId(1L);
        Totem t2 = new Totem();
        t2.setId(2L);

        List<TotemDTO> dtos = mapper.toDtoList(List.of(t1, t2));

        assertEquals(2, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
    }

    @Test
    void toEntity_DeveCriarEntidade() {
        TotemRequestDTO request = new TotemRequestDTO("Loc", "Desc");

        Totem totem = mapper.toEntity(request);

        assertNotNull(totem);
        assertEquals("Loc", totem.getLocalizacao());
        assertEquals("Desc", totem.getDescricao());
        assertNull(totem.getId()); 
    }

    @Test
    void updateEntityFromDto_DeveAtualizarCampos() {
        Totem totem = new Totem();
        totem.setLocalizacao("Antiga");
        
        TotemRequestDTO request = new TotemRequestDTO("Nova Loc", "Nova Desc");

        mapper.updateEntityFromDto(request, totem);

        assertEquals("Nova Loc", totem.getLocalizacao());
        assertEquals("Nova Desc", totem.getDescricao());
    }

    @Test
    void toDto_ComNull_DeveRetornarNull() {
        assertNull(mapper.toDto(null));
        assertTrue(mapper.toDtoList(null).isEmpty());
    }

    @Test
    void toEntityNull(){
        assertNull(mapper.toEntity(null));
        mapper.updateEntityFromDto(null, null);
    }
}