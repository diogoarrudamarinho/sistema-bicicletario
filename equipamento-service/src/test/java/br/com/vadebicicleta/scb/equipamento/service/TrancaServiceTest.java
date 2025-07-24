package br.com.vadebicicleta.scb.equipamento.service;

import br.com.vadebicicleta.scb.equipamento.dto.*;
import br.com.vadebicicleta.scb.equipamento.entity.Bicicleta;
import br.com.vadebicicleta.scb.equipamento.entity.Totem;
import br.com.vadebicicleta.scb.equipamento.entity.Tranca;
import br.com.vadebicicleta.scb.equipamento.entity.TrancaStatus;
import br.com.vadebicicleta.scb.equipamento.exception.RecursoNaoEncontradoException;
import br.com.vadebicicleta.scb.equipamento.exception.RegraDeNegocioException;
import br.com.vadebicicleta.scb.equipamento.mapper.BicicletaMapper;
import br.com.vadebicicleta.scb.equipamento.mapper.TrancaMapper;
import br.com.vadebicicleta.scb.equipamento.repository.TotemRepository;
import br.com.vadebicicleta.scb.equipamento.repository.TrancaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrancaServiceTest {

    @Mock
    private TrancaRepository trancaRepository;
    @Mock
    private TotemRepository totemRepository;
    @Mock
    private TrancaMapper trancaMapper;
    @Mock
    private BicicletaMapper bicicletaMapper;

    @InjectMocks
    private TrancaService trancaService;

    private Tranca tranca;
    private Totem totem;
    private NovaTrancaDTO novaTrancaDTO;
    private AlteraTrancaDTO alteraTrancaDTO;
    private TrancaIntegrarDTO trancaIntegrarDTO;
    private TrancaRetirarDTO trancaRetirarDTO;
    private TrancaDTO trancaDTO;
    private UUID trancaId;
    private UUID totemId;

    @BeforeEach
    void setUp() {
        trancaId = UUID.randomUUID();
        totemId = UUID.randomUUID();
        UUID funcionarioId = UUID.randomUUID();

        tranca = new Tranca();
        tranca.setId(1L);
        tranca.setPublicId(trancaId);
        tranca.setStatus(TrancaStatus.NOVA);

        totem = new Totem();
        totem.setId(1L);
        totem.setPublicId(totemId);

        trancaIntegrarDTO = new TrancaIntegrarDTO(totemId, funcionarioId);
        trancaRetirarDTO = new TrancaRetirarDTO(funcionarioId, "EM_REPARO");

        novaTrancaDTO = new NovaTrancaDTO();
        alteraTrancaDTO = new AlteraTrancaDTO();
        trancaDTO = new TrancaDTO();
    }

    @Test
    @DisplayName("Cadastrar Tranca: Deve cadastrar com sucesso")
    void cadastrarTranca_ComDadosValidos_DeveRetornarDto() {
        when(trancaMapper.toEntity(any(NovaTrancaDTO.class))).thenReturn(new Tranca());
        when(trancaRepository.save(any(Tranca.class))).thenReturn(tranca);
        when(trancaMapper.toDto(any(Tranca.class))).thenReturn(trancaDTO);

        TrancaDTO resultado = trancaService.cadastrarTranca(novaTrancaDTO);

        assertNotNull(resultado);
        verify(trancaRepository).save(any(Tranca.class));
    }

    @Test
    @DisplayName("Alterar Tranca: Deve alterar com sucesso")
    void alterarTranca_QuandoIdExiste_DeveRetornarDto() {
        when(trancaRepository.findByPublicId(trancaId)).thenReturn(Optional.of(tranca));
        when(trancaRepository.save(any(Tranca.class))).thenReturn(tranca);
        when(trancaMapper.toDto(any(Tranca.class))).thenReturn(trancaDTO);

        trancaService.alterarTranca(trancaId, alteraTrancaDTO);

        verify(trancaMapper).updateEntityFromDto(alteraTrancaDTO, tranca);
        verify(trancaRepository).save(tranca);
    }

    @Test
    @DisplayName("Integrar na Rede: Deve integrar com sucesso")
    void integrarNaRede_ComDadosValidos_DeveAtualizarStatus() {
        when(trancaRepository.findByPublicId(trancaId)).thenReturn(Optional.of(tranca));
        when(totemRepository.findByPublicId(totemId)).thenReturn(Optional.of(totem));

        trancaService.integrarNaRede(trancaId, trancaIntegrarDTO);

        assertEquals(TrancaStatus.LIVRE, tranca.getStatus());
        assertEquals(totem, tranca.getTotem());
        verify(trancaRepository).save(tranca);
    }

    @Test
    @DisplayName("Integrar na Rede: Deve falhar se o status da tranca for inválido")
    void integrarNaRede_ComStatusInvalido_DeveLancarExcecao() {
        tranca.setStatus(TrancaStatus.OCUPADA);
        when(trancaRepository.findByPublicId(trancaId)).thenReturn(Optional.of(tranca));
        when(totemRepository.findByPublicId(totemId)).thenReturn(Optional.of(totem));

        assertThrows(RegraDeNegocioException.class, () -> {
            trancaService.integrarNaRede(trancaId, trancaIntegrarDTO);
        });
    }

    @Test
    @DisplayName("Integrar na Rede: Deve falhar se o totem não for encontrado")
    void integrarNaRede_QuandoTotemNaoExiste_DeveLancarExcecao() {
        when(trancaRepository.findByPublicId(trancaId)).thenReturn(Optional.of(tranca));
        when(totemRepository.findByPublicId(totemId)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> {
            trancaService.integrarNaRede(trancaId, trancaIntegrarDTO);
        });
    }

    @Test
    @DisplayName("Retirar da Rede: Deve retirar com sucesso para reparo")
    void retirarDaRede_ParaReparo_DeveFuncionar() {
        tranca.setStatus(TrancaStatus.LIVRE);
        when(trancaRepository.findByPublicId(trancaId)).thenReturn(Optional.of(tranca));

        trancaService.retirarDaRede(trancaId, trancaRetirarDTO);

        assertEquals(TrancaStatus.EM_REPARO, tranca.getStatus());
        assertNull(tranca.getTotem());
        verify(trancaRepository).save(tranca);
    }

    @Test
    @DisplayName("Retirar da Rede: Deve retirar com sucesso para aposentadoria")
    void retirarDaRede_ParaAposentadoria_DeveFuncionar() {
        tranca.setStatus(TrancaStatus.LIVRE);
        trancaRetirarDTO.setStatusAcaoReparador("APOSENTADA");
        when(trancaRepository.findByPublicId(trancaId)).thenReturn(Optional.of(tranca));

        trancaService.retirarDaRede(trancaId, trancaRetirarDTO);

        assertEquals(TrancaStatus.APOSENTADA, tranca.getStatus());
        verify(trancaRepository).save(tranca);
    }

    @Test
    @DisplayName("Retirar da Rede: Deve falhar se a tranca estiver ocupada")
    void retirarDaRede_QuandoOcupada_DeveLancarExcecao() {
        tranca.setStatus(TrancaStatus.OCUPADA);
        when(trancaRepository.findByPublicId(trancaId)).thenReturn(Optional.of(tranca));

        assertThrows(RegraDeNegocioException.class, () -> {
            trancaService.retirarDaRede(trancaId, trancaRetirarDTO);
        });
        verify(trancaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Retirar da Rede: Deve falhar com ação de status inválida")
    void retirarDaRede_ComAcaoInvalida_DeveLancarExcecao() {
        tranca.setStatus(TrancaStatus.LIVRE);
        trancaRetirarDTO.setStatusAcaoReparador("ACAO_INVALIDA");
        when(trancaRepository.findByPublicId(trancaId)).thenReturn(Optional.of(tranca));

        assertThrows(RegraDeNegocioException.class, () -> {
            trancaService.retirarDaRede(trancaId, trancaRetirarDTO);
        });
    }

    @Test
    @DisplayName("Listar Todas: Deve retornar lista de DTOs")
    void listarTodas_DeveRetornarListaDeDtos() {
        when(trancaRepository.findAll()).thenReturn(Collections.singletonList(tranca));
        when(trancaMapper.toDto(any(Tranca.class))).thenReturn(trancaDTO);

        var resultado = trancaService.listarTodas();

        assertFalse(resultado.isEmpty());
        verify(trancaRepository).findAll();
    }

    @Test
    @DisplayName("Buscar por ID: Deve lançar exceção quando não encontrado")
    void buscarPorId_QuandoNaoEncontrado_DeveLancarExcecao() {
        when(trancaRepository.findByPublicId(trancaId)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> {
            trancaService.buscarPorId(trancaId);
        });
    }

    @Test
    @DisplayName("Obter Bicicleta: Deve obter com sucesso quando bicicleta existe")
    void obterBicicleta_QuandoExiste_DeveRetornarDto() {
        tranca.setBicicleta(new Bicicleta());
        when(trancaRepository.findByPublicId(trancaId)).thenReturn(Optional.of(tranca));
        when(bicicletaMapper.toDto(any(Bicicleta.class))).thenReturn(new BicicletaDTO());

        var resultado = trancaService.obterBicicleta(trancaId);

        assertNotNull(resultado);
        verify(bicicletaMapper).toDto(any(Bicicleta.class));
    }

    @Test
    @DisplayName("Deletar: Deve deletar com sucesso quando tranca está livre")
    void deletar_QuandoLivre_DeveChamarDelete() {
        tranca.setStatus(TrancaStatus.LIVRE);
        when(trancaRepository.findByPublicId(trancaId)).thenReturn(Optional.of(tranca));
        doNothing().when(trancaRepository).delete(tranca);

        trancaService.deletar(trancaId);

        verify(trancaRepository).delete(tranca);
    }
}
