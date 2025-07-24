package br.com.vadebicicleta.scb.equipamento.service;

import br.com.vadebicicleta.scb.equipamento.dto.AlteraTotemDTO;
import br.com.vadebicicleta.scb.equipamento.dto.BicicletaDTO;
import br.com.vadebicicleta.scb.equipamento.dto.NovoTotemDTO;
import br.com.vadebicicleta.scb.equipamento.dto.TotemDTO;
import br.com.vadebicicleta.scb.equipamento.dto.TrancaDTO;
import br.com.vadebicicleta.scb.equipamento.entity.Bicicleta;
import br.com.vadebicicleta.scb.equipamento.entity.Totem;
import br.com.vadebicicleta.scb.equipamento.entity.Tranca;
import br.com.vadebicicleta.scb.equipamento.exception.RecursoNaoEncontradoException;
import br.com.vadebicicleta.scb.equipamento.exception.RegraDeNegocioException;
import br.com.vadebicicleta.scb.equipamento.mapper.BicicletaMapper;
import br.com.vadebicicleta.scb.equipamento.mapper.TotemMapper;
import br.com.vadebicicleta.scb.equipamento.mapper.TrancaMapper;
import br.com.vadebicicleta.scb.equipamento.repository.TotemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TotemServiceTest {

    @Mock
    private TotemRepository totemRepository;
    @Mock
    private TotemMapper totemMapper;
    @Mock
    private TrancaMapper trancaMapper;
    @Mock
    private BicicletaMapper bicicletaMapper;

    @InjectMocks
    private TotemService totemService;

    private Totem totem;
    private NovoTotemDTO novoTotemDTO;
    private AlteraTotemDTO alteraTotemDTO;
    private TotemDTO totemDTO;
    private UUID totemId;

    @BeforeEach
    void setUp() {
        totemId = UUID.randomUUID();

        totem = new Totem();
        totem.setId(1L);
        totem.setPublicId(totemId);
        totem.setLocalizacao("Praça da Sé");

        Tranca tranca = new Tranca();
        tranca.setBicicleta(new Bicicleta());
        totem.getTrancas().add(tranca);

        novoTotemDTO = new NovoTotemDTO();
        novoTotemDTO.setLocalizacao("Praça da Sé");

        alteraTotemDTO = new AlteraTotemDTO();
        alteraTotemDTO.setLocalizacao("Parque Ibirapuera");

        totemDTO = new TotemDTO();
        totemDTO.setId(totemId);
    }

    @Test
    @DisplayName("Cadastrar Totem: Deve cadastrar com sucesso")
    void cadastrarTotem_ComDadosValidos_DeveRetornarDto() {
        when(totemMapper.toEntity(any(NovoTotemDTO.class))).thenReturn(new Totem());
        when(totemRepository.save(any(Totem.class))).thenReturn(totem);
        when(totemMapper.toDto(any(Totem.class))).thenReturn(totemDTO);

        TotemDTO resultado = totemService.cadastrarTotem(novoTotemDTO);

        assertNotNull(resultado);
        assertEquals(totemId, resultado.getId());
        verify(totemRepository).save(any(Totem.class));
    }

    @Test
    @DisplayName("Listar Todos: Deve retornar lista de DTOs")
    void listarTodos_DeveRetornarListaDeDtos() {
        when(totemRepository.findAll()).thenReturn(Collections.singletonList(totem));
        when(totemMapper.toDto(any(Totem.class))).thenReturn(totemDTO);

        List<TotemDTO> resultado = totemService.listarTodos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(totemRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Buscar por ID: Deve retornar DTO quando encontrado")
    void buscarPorId_QuandoEncontrado_DeveRetornarDto() {
        when(totemRepository.findByPublicId(totemId)).thenReturn(Optional.of(totem));
        when(totemMapper.toDto(totem)).thenReturn(totemDTO);

        TotemDTO resultado = totemService.buscarPorId(totemId);

        assertNotNull(resultado);
        assertEquals(totemId, resultado.getId());
        verify(totemRepository).findByPublicId(totemId);
    }

    @Test
    @DisplayName("Buscar por ID: Deve lançar exceção quando não encontrado")
    void buscarPorId_QuandoNaoEncontrado_DeveLancarExcecao() {
        UUID idInexistente = UUID.randomUUID();
        when(totemRepository.findByPublicId(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> {
            totemService.buscarPorId(idInexistente);
        });
    }

    @Test
    @DisplayName("Alterar Totem: Deve alterar com sucesso quando ID existe")
    void alterarTotem_QuandoIdExiste_DeveRetornarDto() {
        when(totemRepository.findByPublicId(totemId)).thenReturn(Optional.of(totem));
        when(totemRepository.save(any(Totem.class))).thenReturn(totem);
        when(totemMapper.toDto(any(Totem.class))).thenReturn(totemDTO);

        totemService.alterarTotem(totemId, alteraTotemDTO);

        verify(totemMapper).updateEntityFromDto(alteraTotemDTO, totem);
        verify(totemRepository).save(totem);
    }

    @Test
    @DisplayName("Deletar Totem: Deve deletar com sucesso quando totem está vazio")
    void deletar_QuandoTotemVazio_DeveChamarDelete() {
        totem.setTrancas(Collections.emptyList());
        when(totemRepository.findByPublicId(totemId)).thenReturn(Optional.of(totem));
        doNothing().when(totemRepository).delete(totem);

        totemService.deletar(totemId);

        verify(totemRepository).delete(totem);
    }

    @Test
    @DisplayName("Deletar Totem: Deve lançar exceção quando totem possui trancas")
    void deletar_QuandoTotemComTrancas_DeveLancarExcecao() {
        when(totemRepository.findByPublicId(totemId)).thenReturn(Optional.of(totem));

        assertThrows(RegraDeNegocioException.class, () -> {
            totemService.deletar(totemId);
        });
        verify(totemRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Listar Trancas: Deve retornar lista de trancas quando totem existe")
    void listarTrancasDoTotem_QuandoTotemExiste_DeveRetornarLista() {
        when(totemRepository.findByPublicId(totemId)).thenReturn(Optional.of(totem));
        when(trancaMapper.toDto(any(Tranca.class))).thenReturn(new TrancaDTO());

        List<TrancaDTO> resultado = totemService.listarTrancasDoTotem(totemId);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(trancaMapper, times(1)).toDto(any(Tranca.class));
    }

    @Test
    @DisplayName("Listar Bicicletas: Deve retornar lista de bicicletas quando totem existe")
    void listarBicicletasDoTotem_QuandoTotemExiste_DeveRetornarLista() {
        when(totemRepository.findByPublicId(totemId)).thenReturn(Optional.of(totem));
        when(bicicletaMapper.toDto(any(Bicicleta.class))).thenReturn(new BicicletaDTO());

        List<BicicletaDTO> resultado = totemService.listarBicicletasDoTotem(totemId);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(bicicletaMapper, times(1)).toDto(any(Bicicleta.class));
    }

    @Test
    @DisplayName("Listar Bicicletas: Deve retornar lista vazia se não houver bicicletas")
    void listarBicicletasDoTotem_QuandoNaoHaBicicletas_DeveRetornarListaVazia() {
        totem.getTrancas().get(0).setBicicleta(null); // Tranca sem bicicleta
        when(totemRepository.findByPublicId(totemId)).thenReturn(Optional.of(totem));

        List<BicicletaDTO> resultado = totemService.listarBicicletasDoTotem(totemId);

        assertTrue(resultado.isEmpty());
        verify(bicicletaMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("Restaurar Banco: Deve chamar o método deleteAll do repositório")
    void restaurarBanco_DeveChamarDeleteAll() {
        doNothing().when(totemRepository).deleteAll();

        totemService.restaurarBanco();

        verify(totemRepository, times(1)).deleteAll();
    }
}
