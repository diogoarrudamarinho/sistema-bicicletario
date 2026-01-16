package dev.unirio.equipmentservice.service;

import java.util.List;
import java.util.Optional;

import org.hibernate.ObjectNotFoundException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.unirio.equipmentservice.dto.BicicletaDTO;
import dev.unirio.equipmentservice.dto.TrancaDTO;
import dev.unirio.equipmentservice.dto.TrancaIntegracaoDTO;
import dev.unirio.equipmentservice.dto.TrancaRequestDTO;
import dev.unirio.equipmentservice.entity.Bicicleta;
import dev.unirio.equipmentservice.entity.Totem;
import dev.unirio.equipmentservice.entity.Tranca;
import dev.unirio.equipmentservice.enumeration.BicicletaStatus;
import dev.unirio.equipmentservice.enumeration.TrancaStatus;
import dev.unirio.equipmentservice.exception.NegocioException;
import dev.unirio.equipmentservice.mapper.BicicletaMapper;
import dev.unirio.equipmentservice.mapper.TrancaMapper;
import dev.unirio.equipmentservice.repository.BicicletaRepository;
import dev.unirio.equipmentservice.repository.TotemRepository;
import dev.unirio.equipmentservice.repository.TrancaRepository;
import dev.unirio.equipmentservice.service.implementation.TrancaServiceImplementation;

@ExtendWith(MockitoExtension.class)
class TrancaServiceTest {

    private static final Long ID = 1L;

    @Mock
    private TrancaRepository repository;

    @Mock
    private BicicletaRepository bicicletaRepository;

    @Mock
    private TotemRepository totemRepository;

    @Mock
    private TrancaMapper mapper;

    @Mock
    private BicicletaMapper bicicletaMapper;

    @InjectMocks
    private TrancaServiceImplementation service;

    private Tranca tranca;
    private Bicicleta bicicleta;
    private Totem totem;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        tranca = new Tranca();
        tranca.setId(ID);
        tranca.setStatus(TrancaStatus.LIVRE);

        bicicleta = new Bicicleta();
        bicicleta.setId(10L);
        bicicleta.setStatus(BicicletaStatus.NOVA);

        totem = new Totem();
        totem.setId(5L);
    }

    /* ================= BUSCAR ================= */

    @Test
    void buscarTrancas() {
        when(repository.findAll()).thenReturn(List.of(tranca));
        when(mapper.toDtoList(any())).thenReturn(List.of(new TrancaDTO()));

        List<TrancaDTO> result = service.buscarTrancas();

        assertEquals(1, result.size());
        verify(repository).findAll();
    }

    @Test
    void buscarTranca() {
        when(repository.findById(ID)).thenReturn(Optional.of(tranca));
        when(mapper.toDto(tranca)).thenReturn(new TrancaDTO());

        assertNotNull(service.buscarTranca(ID));
    }

    @Test
    void buscarTrancaIdNull() {
        Throwable exception = assertThrows(NegocioException.class, () -> service.buscarTranca(null));
        assertNotNull(exception);
    }

    @Test
    void buscarTrancaNotFound() {
        when(repository.findById(ID)).thenReturn(Optional.empty());
        Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.buscarTranca(ID));
        assertNotNull(exception);
    }

    /* ================= BUSCAR ENTIDADE ================= */

    @Test
    void buscarEntidade() {
        when(repository.findById(ID)).thenReturn(Optional.of(tranca));
        assertNotNull(service.buscarEntidade(ID));
    }

    @Test
    void buscarEntidadeIdNull() {

        Throwable exception = assertThrows(
            NegocioException.class,
            () -> service.buscarEntidade(null));

        assertNotNull(exception);
    }

    @Test
    void buscarEntidadeNotFound() {

        when(repository.findById(ID)).thenReturn(Optional.empty());

        Throwable exception = assertThrows(
            ObjectNotFoundException.class,
            () -> service.buscarEntidade(ID));

        assertNotNull(exception);
    }
    
    /* ================= BUSCAR BICICLETA ================= */

    @Test
    void buscarBicicleta() {
        tranca.setBicicleta(bicicleta);

        when(repository.findById(ID)).thenReturn(Optional.of(tranca));
        when(bicicletaMapper.toDto(bicicleta)).thenReturn(new BicicletaDTO());

        assertNotNull(service.buscarBicicleta(ID));
    }

    @Test
    void buscarBicicletaIdNull() {

        Throwable exception = assertThrows(
            NegocioException.class,
            () -> service.buscarBicicleta(null));

        assertNotNull(exception);
    }

    @Test
    void buscarBicicletaNotFound() {

        when(repository.findById(ID)).thenReturn(Optional.empty());

        Throwable exception = assertThrows(
            ObjectNotFoundException.class,
            () -> service.buscarBicicleta(ID));

        assertNotNull(exception);
    }

    @Test
    void buscarBicicletaSemBicicleta() {
        when(repository.findById(ID)).thenReturn(Optional.of(tranca));
        Throwable exception = assertThrows(NegocioException.class, () -> service.buscarBicicleta(ID));
        assertNotNull(exception);
    }

    /* ================= BUSCAR POR TOTEM ================= */

    @Test
    void buscarBicicletasPorTotem() {
        tranca.setBicicleta(bicicleta);

        when(repository.findAllByTotemId(ID))
            .thenReturn(List.of(tranca));
        when(bicicletaMapper.toDto(bicicleta))
            .thenReturn(new BicicletaDTO());

        List<BicicletaDTO> resultado = service.buscarBicicletasPorTotem(ID);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(repository).findAllByTotemId(ID);
        verify(bicicletaMapper).toDto(bicicleta);
    }

    @Test
    void buscarBicicletasPorTotemSemBicicleta() {
        when(repository.findAllByTotemId(ID))
            .thenReturn(List.of(tranca));

        List<BicicletaDTO> resultado = service.buscarBicicletasPorTotem(ID);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(repository).findAllByTotemId(ID);
        verify(bicicletaMapper, never()).toDto(any());
    }

    @Test
    void buscarBicicletasPorTotemListaVazia() {
        when(repository.findAllByTotemId(ID))
            .thenReturn(List.of());

        List<BicicletaDTO> resultado = service.buscarBicicletasPorTotem(ID);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(repository).findAllByTotemId(ID);
    }

    @Test
    void buscarTrancasPorTotem() {
        TrancaDTO trancaDTO = new TrancaDTO();

        when(repository.findAllByTotemId(ID))
            .thenReturn(List.of(tranca));
        when(mapper.toDtoList(List.of(tranca)))
            .thenReturn(List.of(trancaDTO));

        List<TrancaDTO> resultado = service.buscarTrancasPorTotem(ID);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(repository).findAllByTotemId(ID);
        verify(mapper).toDtoList(List.of(tranca));
    }

    @Test
    void buscarTrancasPorTotemListaVazia() {
        when(repository.findAllByTotemId(ID))
            .thenReturn(List.of());
        when(mapper.toDtoList(List.of()))
            .thenReturn(List.of());

        List<TrancaDTO> resultado = service.buscarTrancasPorTotem(ID);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(repository).findAllByTotemId(ID);
    }

    /* ================= CADASTRAR ================= */

    @Test
    void cadastrarTranca() {
        TrancaRequestDTO dto = new TrancaRequestDTO();
        dto.setStatus(TrancaStatus.NOVA);

        when(mapper.toEntity(dto)).thenReturn(tranca);
        when(repository.save(tranca)).thenReturn(tranca);
        when(mapper.toDto(tranca)).thenReturn(new TrancaDTO());

        assertNotNull(service.cadastrarTranca(dto));
    }

    @Test
    void cadastrarTrancaStatusInvalido() {
        TrancaRequestDTO dto = new TrancaRequestDTO();
        dto.setStatus(TrancaStatus.LIVRE);

        Throwable exception = assertThrows(NegocioException.class, () -> service.cadastrarTranca(dto));
        assertNotNull(exception);
    }

    @Test
    void cadastrarTrancaNull() {
        TrancaRequestDTO dto = new TrancaRequestDTO();
        dto.setStatus(TrancaStatus.NOVA);

        when(mapper.toEntity(dto)).thenReturn(null);

        Throwable exception = assertThrows(
            NegocioException.class, 
            () -> service.cadastrarTranca(dto));
        assertNotNull(exception);
    }

    /* ================= TRANCAR ================= */

    @Test
    void trancarComBicicleta() {
        when(repository.findById(ID)).thenReturn(Optional.of(tranca));
        when(bicicletaRepository.findById(10L)).thenReturn(Optional.of(bicicleta));
        when(repository.save(tranca)).thenReturn(tranca);
        when(mapper.toDto(tranca)).thenReturn(new TrancaDTO());

        TrancaDTO dto = service.trancar(ID, 10L);

        assertEquals(TrancaStatus.OCUPADA, tranca.getStatus());
        assertEquals(BicicletaStatus.DISPONIVEL, bicicleta.getStatus());
        assertNotNull(dto);
    }

    @Test
    void trancarIdNull() {
        Throwable exception = assertThrows(
            NegocioException.class, 
            () -> service.trancar(null, ID));
        assertNotNull(exception);
    }

    @Test
    void trancarTrancaNotFound() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        Throwable exception = assertThrows(
            ObjectNotFoundException.class, 
            () -> service.trancar(ID, ID));
        assertNotNull(exception);
    }

    @Test
    void trancarBicicletaNotFound() {
        tranca.setBicicleta(null);
        
        when(bicicletaRepository.findById(ID)).thenReturn(Optional.empty());
        when(repository.findById(ID)).thenReturn(Optional.of(tranca));

        Throwable exception = assertThrows(
            ObjectNotFoundException.class, 
            () -> service.trancar(ID, ID));
        assertNotNull(exception);
    }

    @Test
    void trancarTrancaComDefeito() {
        tranca.setStatus(TrancaStatus.REPARO_SOLICITADO);
        when(repository.findById(ID)).thenReturn(Optional.of(tranca));

        Throwable exception = assertThrows(NegocioException.class, () -> service.trancar(ID, ID));
        assertNotNull(exception);
    }

    @Test
    void trancarTrancaOcupada() {
        tranca.setBicicleta(bicicleta);
        when(repository.findById(ID)).thenReturn(Optional.of(tranca));

        Throwable exception = assertThrows(NegocioException.class, () -> service.trancar(ID, 10L));
        assertNotNull(exception);
    }

    /* ================= DESTRANCAR ================= */

    @Test
    void destrancar() {
        when(repository.findById(ID)).thenReturn(Optional.of(tranca));
        when(bicicletaRepository.findById(10L)).thenReturn(Optional.of(bicicleta));
        when(repository.save(tranca)).thenReturn(tranca);
        when(mapper.toDto(tranca)).thenReturn(new TrancaDTO());

        TrancaDTO dto = service.destrancar(ID, 10L);

        assertEquals(TrancaStatus.LIVRE, tranca.getStatus());
        assertNull(tranca.getBicicleta());
        assertEquals(BicicletaStatus.EM_USO, bicicleta.getStatus());
        assertNotNull(dto);
    }

    @Test
    void destrancarIdNull() {
        Throwable exception = assertThrows(
            NegocioException.class,
            () -> service.destrancar(null, ID)
        );

        assertNotNull(exception);
        verify(repository, never()).findById(any());
    }

    @Test
    void destrancarTrancaNotFound() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        Throwable exception = assertThrows(
            ObjectNotFoundException.class,
            () -> service.destrancar(ID, null)
        );

        assertNotNull(exception);
        verify(repository).findById(ID);
        verify(bicicletaRepository, never()).findById(any());
    }

    @Test
    void destrancarBicicletaNotFound() {
        tranca.setId(ID);

        when(repository.findById(ID)).thenReturn(Optional.of(tranca));
        when(bicicletaRepository.findById(ID))
            .thenReturn(Optional.empty());

        Throwable exception = assertThrows(
            ObjectNotFoundException.class,
            () -> service.destrancar(ID, ID)
        );

        assertNotNull(exception);
        verify(repository).findById(ID);
        verify(bicicletaRepository).findById(ID);
        verify(repository, never()).save(any());
    }

    /* ================= ALTERAR STATUS ================= */

    @Test
    void alterarStatusAposentada() {
        when(repository.findById(ID)).thenReturn(Optional.of(tranca));
        when(repository.save(tranca)).thenReturn(tranca);
        when(mapper.toDto(tranca)).thenReturn(new TrancaDTO());

        service.alterarStatus(ID, TrancaStatus.APOSENTADA);

        assertEquals(TrancaStatus.APOSENTADA, tranca.getStatus());
    }

    @Test
    void alterarStatusIdNull(){
        Throwable exception = assertThrows(NegocioException.class,
            () -> service.alterarStatus(null, TrancaStatus.LIVRE));
        assertNotNull(exception);
    }

    @Test
    void alterarStatusInvalidoLivre() {
        Throwable exception = assertThrows(NegocioException.class,
            () -> service.alterarStatus(ID, TrancaStatus.LIVRE));
        assertNotNull(exception);
        verify(repository, org.mockito.Mockito.never()).save(any());
    }

    @Test
    void alterarStatusInvalidoOcupado() {
        Throwable exception = assertThrows(NegocioException.class,
            () -> service.alterarStatus(ID, TrancaStatus.OCUPADA));
        assertNotNull(exception);
        verify(repository, org.mockito.Mockito.never()).save(any());
    }

    /* ================= INTEGRAR REDE ================= */

    @Test
    void integrarRede() {
        TrancaIntegracaoDTO dto = new TrancaIntegracaoDTO();
        dto.setTranca(ID);
        dto.setTotem(5L);

        tranca.setStatus(TrancaStatus.NOVA);

        when(repository.findById(ID)).thenReturn(Optional.of(tranca));
        when(totemRepository.findById(5L)).thenReturn(Optional.of(totem));

        service.integrarRede(dto);

        assertEquals(totem, tranca.getTotem());
        assertEquals(TrancaStatus.LIVRE, tranca.getStatus());
    }

    @Test
    void integrarRedeTotemNull() {
        TrancaIntegracaoDTO dto = new TrancaIntegracaoDTO();
        dto.setTranca(ID);
        dto.setTotem(null);

        Throwable exception = assertThrows(
            NegocioException.class,
            () -> service.integrarRede(dto)
        );

        assertNotNull(exception);
        verify(repository, never()).findById(any());
        verify(totemRepository, never()).findById(any());
    }

    @Test
    void integrarRedeTrancaNotFound() {
        TrancaIntegracaoDTO dto = new TrancaIntegracaoDTO();
        dto.setTranca(ID);
        dto.setTotem(ID);

        when(repository.findById(ID)).thenReturn(Optional.empty());

        Throwable exception = assertThrows(
            ObjectNotFoundException.class,
            () -> service.integrarRede(dto)
        );

        assertNotNull(exception);
        verify(repository).findById(ID);
        verify(totemRepository, never()).findById(any());
    }

    @Test
    void integrarRedeTotemNotFound() {
        TrancaIntegracaoDTO dto = new TrancaIntegracaoDTO();
        dto.setTranca(ID);
        dto.setTotem(ID);

        tranca.setId(ID);
        tranca.setStatus(TrancaStatus.NOVA);

        when(repository.findById(ID)).thenReturn(Optional.of(tranca));
        when(totemRepository.findById(ID)).thenReturn(Optional.empty());

        Throwable exception = assertThrows(
            ObjectNotFoundException.class,
            () -> service.integrarRede(dto)
        );

        assertNotNull(exception);
        verify(repository).findById(ID);
        verify(totemRepository).findById(ID);
        verify(repository, never()).save(any());
    }

    /* ================= RETIRAR REDE ================= */

    @Test
    void retirarRede() {
        TrancaIntegracaoDTO dto = new TrancaIntegracaoDTO();
        dto.setTranca(ID);
        dto.setStatus(TrancaStatus.APOSENTADA);

        when(repository.findById(ID)).thenReturn(Optional.of(tranca));

        service.retirarRede(dto);

        assertEquals(TrancaStatus.APOSENTADA, tranca.getStatus());
        assertNull(tranca.getTotem());
    }

    @Test
    void retirarRedeStatusNull() {
        TrancaIntegracaoDTO dto = new TrancaIntegracaoDTO();
        dto.setTranca(ID);
        dto.setStatus(null);

        Throwable exception = assertThrows(
            NegocioException.class,
            () -> service.retirarRede(dto)
        );

        assertNotNull(exception);
        verify(repository, never()).findById(any());
    }

    @Test
    void retirarRedeTrancaNotFound() {
        TrancaIntegracaoDTO dto = new TrancaIntegracaoDTO();
        dto.setTranca(ID);
        dto.setStatus(TrancaStatus.EM_REPARO);

        when(repository.findById(ID)).thenReturn(Optional.empty());

        Throwable exception = assertThrows(
            ObjectNotFoundException.class,
            () -> service.retirarRede(dto)
        );

        assertNotNull(exception);
        verify(repository).findById(ID);
        verify(repository, never()).save(any());
    }

    @Test
    void retirarRedeTrancaOcupada() {
        TrancaIntegracaoDTO dto = new TrancaIntegracaoDTO();
        dto.setTranca(ID);
        dto.setStatus(TrancaStatus.EM_REPARO);

        tranca.setId(ID);
        tranca.setStatus(TrancaStatus.OCUPADA);

        when(repository.findById(ID)).thenReturn(Optional.of(tranca));

        Throwable exception = assertThrows(
            NegocioException.class,
            () -> service.retirarRede(dto)
        );

        assertNotNull(exception);
        verify(repository).findById(ID);
        verify(repository, never()).save(any());
    }

    /* ================= ATUALIZAR ================= */

    @Test
    void atualizarTranca() {
        TrancaRequestDTO dto = new TrancaRequestDTO();

        when(repository.findById(ID)).thenReturn(Optional.of(tranca));
        when(repository.save(tranca)).thenReturn(tranca);
        when(mapper.toDto(tranca)).thenReturn(new TrancaDTO());

        assertNotNull(service.atualizarTranca(ID, dto));
    }

    @Test
    void atualizarTrancaIdOuDtoNull() {
        TrancaRequestDTO dto = new TrancaRequestDTO();

        Throwable ex1 = assertThrows(
            NegocioException.class,
            () -> service.atualizarTranca(null, dto)
        );

        Throwable ex2 = assertThrows(
            NegocioException.class,
            () -> service.atualizarTranca(ID, null)
        );

        verify(repository, never()).findById(any());
        assertNotNull(ex1);
        assertNotNull(ex2);
    }

    @Test
    void atualizarTrancaNotFound() {
        TrancaRequestDTO dto = new TrancaRequestDTO();

        when(repository.findById(ID)).thenReturn(Optional.empty());

        Throwable exception = assertThrows(
            ObjectNotFoundException.class,
            () -> service.atualizarTranca(ID, dto)
        );

        assertNotNull(exception);
        verify(repository).findById(ID);
        verify(repository, never()).save(any());
    }

    /* ================= DELETAR ================= */

    @Test
    void deletar() {
        when(repository.existsById(ID)).thenReturn(true);
        service.deletar(ID);
        verify(repository).deleteById(ID);
    }

    @Test
    void deletarIdNull() {
        Throwable exception = assertThrows(
            NegocioException.class,
            () -> service.deletar(null));

        assertNotNull(exception);
    }

    @Test
    void deletarNotFound() {
        when(repository.existsById(ID)).thenReturn(false);
        Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.deletar(ID));
        assertNotNull(exception);
    }
}
