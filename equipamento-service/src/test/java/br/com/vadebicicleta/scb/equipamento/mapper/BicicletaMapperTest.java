package br.com.vadebicicleta.scb.equipamento.mapper;

import br.com.vadebicicleta.scb.equipamento.dto.AlteraBicicletaDTO;
import br.com.vadebicicleta.scb.equipamento.dto.BicicletaDTO;
import br.com.vadebicicleta.scb.equipamento.dto.NovaBicicletaDTO;
import br.com.vadebicicleta.scb.equipamento.entity.Bicicleta;
import br.com.vadebicicleta.scb.equipamento.entity.BicicletaStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BicicletaMapperTest {

    private BicicletaMapper bicicletaMapper;

    @BeforeEach
    void setUp() {
        // Instanciamos o mapper diretamente, pois ele não tem dependências.
        bicicletaMapper = new BicicletaMapper();
    }

    @Test
    @DisplayName("Deve mapear corretamente de Entidade Bicicleta para BicicletaDTO")
    void toDto_ComEntidadeValida_DeveRetornarDtoCorreto() {
        // ARRANGE
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setPublicId(UUID.randomUUID());
        bicicleta.setMarca("Caloi");
        bicicleta.setModelo("10");
        bicicleta.setAno(2023);
        bicicleta.setStatus(BicicletaStatus.DISPONIVEL);

        // ACT
        BicicletaDTO dto = bicicletaMapper.toDto(bicicleta);

        // ASSERT
        assertNotNull(dto);
        assertEquals(bicicleta.getId(), dto.getId());
        assertEquals(bicicleta.getMarca(), dto.getMarca());
        assertEquals(bicicleta.getModelo(), dto.getModelo());
        assertEquals(bicicleta.getAno(), dto.getAno());
        assertEquals(bicicleta.getStatus().name(), dto.getStatus());
    }

    @Test
    @DisplayName("Deve retornar nulo quando a Entidade Bicicleta for nula")
    void toDto_ComEntidadeNula_DeveRetornarNulo() {
        // ACT
        BicicletaDTO dto = bicicletaMapper.toDto(null);

        // ASSERT
        assertNull(dto);
    }

    @Test
    @DisplayName("Deve mapear corretamente de NovaBicicletaDTO para Entidade Bicicleta")
    void toEntity_ComDtoValido_DeveRetornarEntidadeCorreta() {
        // ARRANGE
        NovaBicicletaDTO dto = new NovaBicicletaDTO();
        dto.setMarca("Monark");
        dto.setModelo("BMX");
        dto.setAno(2024);

        // ACT
        Bicicleta bicicleta = bicicletaMapper.toEntity(dto);

        // ASSERT
        assertNotNull(bicicleta);
        assertEquals(dto.getMarca(), bicicleta.getMarca());
        assertEquals(dto.getModelo(), bicicleta.getModelo());
        assertEquals(dto.getAno(), bicicleta.getAno());
        // Verifica que os campos não preenchidos pelo DTO são nulos
        assertNull(bicicleta.getPublicId());
        assertNull(bicicleta.getStatus());
    }

    @Test
    @DisplayName("Deve retornar nulo quando o NovaBicicletaDTO for nulo")
    void toEntity_ComDtoNulo_DeveRetornarNulo() {
        // ACT
        Bicicleta bicicleta = bicicletaMapper.toEntity(null);

        // ASSERT
        assertNull(bicicleta);
    }

    @Test
    @DisplayName("Deve atualizar uma Entidade a partir de um AlteraBicicletaDTO")
    void updateEntityFromDto_ComDadosValidos_DeveAtualizarEntidade() {
        // ARRANGE
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setMarca("Marca Antiga");
        bicicleta.setModelo("Modelo Antigo");
        bicicleta.setAno(2000);

        AlteraBicicletaDTO dto = new AlteraBicicletaDTO();
        dto.setMarca("Marca Nova");
        dto.setModelo("Modelo Novo");
        dto.setAno(2025);

        // ACT
        bicicletaMapper.updateEntityFromDto(dto, bicicleta);

        // ASSERT
        assertEquals("Marca Nova", bicicleta.getMarca());
        assertEquals("Modelo Novo", bicicleta.getModelo());
        assertEquals(2025, bicicleta.getAno());
    }
}
