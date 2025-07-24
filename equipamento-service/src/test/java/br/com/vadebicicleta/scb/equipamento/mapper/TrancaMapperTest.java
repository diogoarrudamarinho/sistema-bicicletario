package br.com.vadebicicleta.scb.equipamento.mapper;

import br.com.vadebicicleta.scb.equipamento.dto.AlteraTrancaDTO;
import br.com.vadebicicleta.scb.equipamento.dto.NovaTrancaDTO;
import br.com.vadebicicleta.scb.equipamento.dto.TrancaDTO;
import br.com.vadebicicleta.scb.equipamento.entity.Totem;
import br.com.vadebicicleta.scb.equipamento.entity.Tranca;
import br.com.vadebicicleta.scb.equipamento.entity.TrancaStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TrancaMapperTest {

    private TrancaMapper trancaMapper;

    @BeforeEach
    void setUp() {
        trancaMapper = new TrancaMapper();
    }

    @Test
    @DisplayName("Deve mapear corretamente de Entidade Tranca para TrancaDTO")
    void toDto_ComEntidadeValida_DeveRetornarDtoCorreto() {
        // ARRANGE
        Tranca tranca = new Tranca();
        tranca.setPublicId(UUID.randomUUID());
        tranca.setNumero("101");
        tranca.setModelo("Modelo Forte");
        tranca.setAnoDeFabricacao(2022);
        tranca.setStatus(TrancaStatus.LIVRE);

        // ACT
        TrancaDTO dto = trancaMapper.toDto(tranca);

        // ASSERT
        assertNotNull(dto);
        assertEquals(tranca.getPublicId(), dto.getId());
        assertEquals(tranca.getNumero(), dto.getNumero());
        assertEquals(tranca.getModelo(), dto.getModelo());
        assertEquals(tranca.getAnoDeFabricacao(), dto.getAnoDeFabricacao());
        assertEquals(tranca.getStatus().name(), dto.getStatus());
        assertNull(dto.getIdTotem()); // Verifica que o id do totem é nulo quando não há totem
    }

    @Test
    @DisplayName("Deve mapear idTotem quando a Tranca está associada a um Totem")
    void toDto_ComTotemAssociado_DeveMapearIdTotem() {
        // ARRANGE
        Totem totem = new Totem();
        totem.setPublicId(UUID.randomUUID());

        Tranca tranca = new Tranca();
        tranca.setPublicId(UUID.randomUUID());
        tranca.setStatus(TrancaStatus.LIVRE);
        tranca.setTotem(totem);

        // ACT
        TrancaDTO dto = trancaMapper.toDto(tranca);

        // ASSERT
        assertNotNull(dto);
        assertEquals(totem.getPublicId(), dto.getIdTotem());
    }

    @Test
    @DisplayName("Deve retornar nulo quando a Entidade Tranca for nula")
    void toDto_ComEntidadeNula_DeveRetornarNulo() {
        // ACT
        TrancaDTO dto = trancaMapper.toDto(null);

        // ASSERT
        assertNull(dto);
    }

    @Test
    @DisplayName("Deve mapear corretamente de NovaTrancaDTO para Entidade Tranca")
    void toEntity_ComDtoValido_DeveRetornarEntidadeCorreta() {
        // ARRANGE
        NovaTrancaDTO dto = new NovaTrancaDTO();
        dto.setNumero("102");
        dto.setModelo("Modelo Leve");
        dto.setAnoDeFabricacao(2023);

        // ACT
        Tranca tranca = trancaMapper.toEntity(dto);

        // ASSERT
        assertNotNull(tranca);
        assertEquals(dto.getNumero(), tranca.getNumero());
        assertEquals(dto.getModelo(), tranca.getModelo());
        assertEquals(dto.getAnoDeFabricacao(), tranca.getAnoDeFabricacao());
        assertNull(tranca.getPublicId());
        assertNull(tranca.getStatus());
    }

    @Test
    @DisplayName("Deve retornar nulo quando o NovaTrancaDTO for nulo")
    void toEntity_ComDtoNulo_DeveRetornarNulo() {
        // ACT
        Tranca tranca = trancaMapper.toEntity(null);

        // ASSERT
        assertNull(tranca);
    }

    @Test
    @DisplayName("Deve atualizar uma Entidade a partir de um AlteraTrancaDTO")
    void updateEntityFromDto_ComDadosValidos_DeveAtualizarEntidade() {
        // ARRANGE
        Tranca tranca = new Tranca();
        tranca.setNumero("Num Antigo");
        tranca.setModelo("Modelo Antigo");
        tranca.setAnoDeFabricacao(2000);

        AlteraTrancaDTO dto = new AlteraTrancaDTO();
        dto.setNumero("Num Novo");
        dto.setModelo("Modelo Novo");
        dto.setAnoDeFabricacao(2025);

        // ACT
        trancaMapper.updateEntityFromDto(dto, tranca);

        // ASSERT
        assertEquals("Num Novo", tranca.getNumero());
        assertEquals("Modelo Novo", tranca.getModelo());
        assertEquals(2025, tranca.getAnoDeFabricacao());
    }
}
