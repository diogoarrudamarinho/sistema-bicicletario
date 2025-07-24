package br.com.vadebicicleta.scb.equipamento.service;

import br.com.vadebicicleta.scb.equipamento.dto.AlteraBicicletaDTO;
import br.com.vadebicicleta.scb.equipamento.dto.BicicletaDTO;
import br.com.vadebicicleta.scb.equipamento.dto.IntegrarNaRedeDTO;
import br.com.vadebicicleta.scb.equipamento.dto.NovaBicicletaDTO;
import br.com.vadebicicleta.scb.equipamento.dto.RetirarDaRedeDTO;
import br.com.vadebicicleta.scb.equipamento.entity.Bicicleta;
import br.com.vadebicicleta.scb.equipamento.entity.BicicletaStatus;
import br.com.vadebicicleta.scb.equipamento.entity.Tranca;
import br.com.vadebicicleta.scb.equipamento.entity.TrancaStatus;
import br.com.vadebicicleta.scb.equipamento.entity.Totem;
import br.com.vadebicicleta.scb.equipamento.exception.RecursoNaoEncontradoException;
import br.com.vadebicicleta.scb.equipamento.exception.RegraDeNegocioException;
import br.com.vadebicicleta.scb.equipamento.mapper.BicicletaMapper;
import br.com.vadebicicleta.scb.equipamento.repository.BicicletaRepository;
import br.com.vadebicicleta.scb.equipamento.repository.TrancaRepository;
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
public class BicicletaServiceTest {

	@Mock
	private BicicletaRepository bicicletaRepository;
	@Mock
	private TrancaRepository trancaRepository;
	@Mock
	private BicicletaMapper bicicletaMapper;

	@InjectMocks
	private BicicletaService bicicletaService;

	private Bicicleta bicicleta;
	private Tranca tranca;
	private NovaBicicletaDTO novaBicicletaDTO;
	private AlteraBicicletaDTO alteraBicicletaDTO;
	private IntegrarNaRedeDTO integrarNaRedeDTO;
	private RetirarDaRedeDTO retirarDaRedeDTO;
	private UUID bicicletaId;
	private UUID trancaId;

	@BeforeEach
	void setUp() {
		bicicletaId = UUID.randomUUID();
		trancaId = UUID.randomUUID();
		UUID funcionarioId = UUID.randomUUID();
		UUID totemId = UUID.randomUUID(); // ID para o totem

		bicicleta = new Bicicleta();
		bicicleta.setId(1L);
		bicicleta.setPublicId(bicicletaId);
		bicicleta.setStatus(BicicletaStatus.NOVA);

		// CORREÇÃO: Inicializa o totem com um publicId
		Totem totem = new Totem();
		totem.setId(3L);
		totem.setPublicId(totemId);

		tranca = new Tranca();
		tranca.setId(2L);
		tranca.setPublicId(trancaId);
		tranca.setStatus(TrancaStatus.LIVRE);
		tranca.setTotem(totem); // Associa o totem com ID à tranca

		novaBicicletaDTO = new NovaBicicletaDTO();
		alteraBicicletaDTO = new AlteraBicicletaDTO();

		integrarNaRedeDTO = new IntegrarNaRedeDTO();
		integrarNaRedeDTO.setIdFuncionario(funcionarioId);
		integrarNaRedeDTO.setIdBicicleta(bicicletaId);
		integrarNaRedeDTO.setIdTranca(trancaId);

		retirarDaRedeDTO = new RetirarDaRedeDTO();
		retirarDaRedeDTO.setIdFuncionario(funcionarioId);
		retirarDaRedeDTO.setIdBicicleta(bicicletaId);
		retirarDaRedeDTO.setIdTranca(trancaId);
		retirarDaRedeDTO.setStatusAcaoReparador("EM_REPARO");
	}

	@Test
	@DisplayName("Cadastrar Bicicleta: Deve cadastrar com sucesso")
	void cadastrarBicicleta_DeveRetornarDto() {
		when(bicicletaMapper.toEntity(any(NovaBicicletaDTO.class))).thenReturn(new Bicicleta());
		when(bicicletaRepository.save(any(Bicicleta.class))).thenReturn(bicicleta);
		when(bicicletaMapper.toDto(any(Bicicleta.class))).thenReturn(new BicicletaDTO());

		BicicletaDTO resultado = bicicletaService.cadastrarBicicleta(novaBicicletaDTO);

		assertNotNull(resultado);
		verify(bicicletaRepository).save(any(Bicicleta.class));
	}

	@Test
	@DisplayName("Integrar na Rede: Deve integrar com sucesso quando bicicleta está EM_REPARO")
	void integrarNaRede_ComStatusEmReparo_DeveFuncionar() {
		bicicleta.setStatus(BicicletaStatus.EM_REPARO);
		when(bicicletaRepository.findByPublicId(bicicletaId)).thenReturn(Optional.of(bicicleta));
		when(trancaRepository.findByPublicId(trancaId)).thenReturn(Optional.of(tranca));

		bicicletaService.integrarNaRede(integrarNaRedeDTO);

		assertEquals(BicicletaStatus.DISPONIVEL, bicicleta.getStatus());
		verify(bicicletaRepository).save(bicicleta);
	}

	@Test
	@DisplayName("Integrar na Rede: Deve falhar se a tranca não for encontrada")
	void integrarNaRede_QuandoTrancaNaoEncontrada_DeveLancarExcecao() {
		when(bicicletaRepository.findByPublicId(bicicletaId)).thenReturn(Optional.of(bicicleta));
		when(trancaRepository.findByPublicId(trancaId)).thenReturn(Optional.empty());

		assertThrows(RecursoNaoEncontradoException.class, () -> {
			bicicletaService.integrarNaRede(integrarNaRedeDTO);
		});
	}

	@Test
	@DisplayName("Retirar da Rede: Deve retirar com sucesso para aposentadoria")
	void retirarDaRede_ParaAposentadoria_DeveFuncionar() {
		bicicleta.setStatus(BicicletaStatus.DISPONIVEL);
		bicicleta.setIdTranca(tranca.getId());
		retirarDaRedeDTO.setStatusAcaoReparador("APOSENTADA");
		when(bicicletaRepository.findByPublicId(bicicletaId)).thenReturn(Optional.of(bicicleta));
		when(trancaRepository.findByPublicId(trancaId)).thenReturn(Optional.of(tranca));

		bicicletaService.retirarDaRede(retirarDaRedeDTO);

		assertEquals(BicicletaStatus.APOSENTADA, bicicleta.getStatus());
		verify(bicicletaRepository).save(bicicleta);
	}

	@Test
	@DisplayName("Retirar da Rede: Deve falhar com statusAcaoReparador válido mas não permitido")
	void retirarDaRede_ComAcaoValidaMasNaoPermitida_DeveLancarExcecao() {
		bicicleta.setStatus(BicicletaStatus.DISPONIVEL);
		bicicleta.setIdTranca(tranca.getId());
		retirarDaRedeDTO.setStatusAcaoReparador("DISPONIVEL"); // Status válido, mas não permitido na lógica
		when(bicicletaRepository.findByPublicId(bicicletaId)).thenReturn(Optional.of(bicicleta));
		when(trancaRepository.findByPublicId(trancaId)).thenReturn(Optional.of(tranca));

		assertThrows(RegraDeNegocioException.class, () -> {
			bicicletaService.retirarDaRede(retirarDaRedeDTO);
		});
	}

	@Test
	@DisplayName("Listar Todas: Deve enriquecer DTOs corretamente")
	void listarTodas_ComBicicletasEmTrancas_DeveEnriquecerDto() {
		bicicleta.setIdTranca(tranca.getId());
		when(bicicletaRepository.findAll()).thenReturn(Collections.singletonList(bicicleta));
		when(trancaRepository.findAllById(any())).thenReturn(Collections.singletonList(tranca));

		// CORREÇÃO: Simplificação do mock do mapper
		when(bicicletaMapper.toDto(any(Bicicleta.class))).thenAnswer(invocation -> {
			Bicicleta b = invocation.getArgument(0);
			BicicletaDTO dto = new BicicletaDTO();
			dto.setId(b.getPublicId());
			return dto;
		});

		List<BicicletaDTO> resultado = bicicletaService.listarTodas();

		assertFalse(resultado.isEmpty());
		assertEquals(tranca.getPublicId(), resultado.get(0).getIdTranca());
		assertNotNull(resultado.get(0).getIdTotem());
	}

	@Test
	@DisplayName("Listar Todas: Deve funcionar com lista vazia")
	void listarTodas_QuandoVazio_DeveRetornarListaVazia() {
		when(bicicletaRepository.findAll()).thenReturn(Collections.emptyList());

		List<BicicletaDTO> resultado = bicicletaService.listarTodas();

		assertTrue(resultado.isEmpty());
	}

	@Test
	@DisplayName("Buscar por ID: Deve enriquecer DTO quando bicicleta está em tranca")
	void buscarPorId_ComBicicletaEmTranca_DeveEnriquecerDto() {
		bicicleta.setIdTranca(tranca.getId());
		when(bicicletaRepository.findByPublicId(bicicletaId)).thenReturn(Optional.of(bicicleta));
		when(trancaRepository.findById(tranca.getId())).thenReturn(Optional.of(tranca));
		// CORREÇÃO: Simplificação do mock do mapper
		when(bicicletaMapper.toDto(bicicleta)).thenReturn(new BicicletaDTO());

		BicicletaDTO resultado = bicicletaService.buscarPorId(bicicletaId);

		assertNotNull(resultado.getIdTranca());
		assertNotNull(resultado.getIdTotem());
	}

	@Test
	@DisplayName("Deletar: Deve deletar com sucesso")
	void deletar_ComStatusAposentadaEForaDaTranca_DeveFuncionar() {
		bicicleta.setStatus(BicicletaStatus.APOSENTADA);
		bicicleta.setIdTranca(null);
		when(bicicletaRepository.findByPublicId(bicicletaId)).thenReturn(Optional.of(bicicleta));
		doNothing().when(bicicletaRepository).delete(bicicleta);

		bicicletaService.deletar(bicicletaId);

		verify(bicicletaRepository).delete(bicicleta);
	}
}
