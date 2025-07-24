package br.com.vadebicicleta.scb.equipamento.mapper;

import br.com.vadebicicleta.scb.equipamento.dto.AlteraTotemDTO;
import br.com.vadebicicleta.scb.equipamento.dto.NovoTotemDTO;
import br.com.vadebicicleta.scb.equipamento.dto.TotemDTO;
import br.com.vadebicicleta.scb.equipamento.entity.Totem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TotemMapperTest {

    private TotemMapper totemMapper;

    @BeforeEach
    void setUp() {
        // Instanciamos o mapper diretamente, pois ele não tem dependências.
        totemMapper = new TotemMapper();
    }

    @Test
    @DisplayName("Deve mapear corretamente de Entidade Totem para TotemDTO")
    void toDto_ComEntidadeValida_DeveRetornarDtoCorreto() {
        // ARRANGE
        Totem totem = new Totem();
        totem.setPublicId(UUID.randomUUID());
        totem.setLocalizacao("Praça da Sé");
        totem.setDescricao("Totem em frente à catedral");

        // ACT
        TotemDTO dto = totemMapper.toDto(totem);

        // ASSERT
        assertNotNull(dto);
        assertEquals(totem.getPublicId(), dto.getId());
        assertEquals(totem.getLocalizacao(), dto.getLocalizacao());
        assertEquals(totem.getDescricao(), dto.getDescricao());
    }

    @Test
    @DisplayName("Deve retornar nulo quando a Entidade Totem for nula")
    void toDto_ComEntidadeNula_DeveRetornarNulo() {
        // ACT
        TotemDTO dto = totemMapper.toDto(null);

        // ASSERT
        assertNull(dto);
    }

    @Test
    @DisplayName("Deve mapear corretamente de NovoTotemDTO para Entidade Totem")
    void toEntity_ComDtoValido_DeveRetornarEntidadeCorreta() {
        // ARRANGE
        NovoTotemDTO dto = new NovoTotemDTO();
        dto.setLocalizacao("Parque Ibirapuera");
        dto.setDescricao("Entrada do portão 3");

        // ACT
        Totem totem = totemMapper.toEntity(dto);

        // ASSERT
        assertNotNull(totem);
        assertEquals(dto.getLocalizacao(), totem.getLocalizacao());
        assertEquals(dto.getDescricao(), totem.getDescricao());
        assertNull(totem.getPublicId());
        assertNull(totem.getId());
    }

    @Test
    @DisplayName("Deve retornar nulo quando o NovoTotemDTO for nulo")
    void toEntity_ComDtoNulo_DeveRetornarNulo() {
        // ACT
        Totem totem = totemMapper.toEntity(null);

        // ASSERT
        assertNull(totem);
    }

    @Test
    @DisplayName("Deve atualizar uma Entidade a partir de um AlteraTotemDTO")
    void updateEntityFromDto_ComDadosValidos_DeveAtualizarEntidade() {
        // ARRANGE
        Totem totem = new Totem();
        totem.setLocalizacao("Localização Antiga");
        totem.setDescricao("Descrição Antiga");

        AlteraTotemDTO dto = new AlteraTotemDTO();
        dto.setLocalizacao("Localização Nova");
        dto.setDescricao("Descrição Nova");

        // ACT
        totemMapper.updateEntityFromDto(dto, totem);

        // ASSERT
        assertEquals("Localização Nova", totem.getLocalizacao());
        assertEquals("Descrição Nova", totem.getDescricao());
    }
}
