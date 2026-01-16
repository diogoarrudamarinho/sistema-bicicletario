package dev.unirio.equipmentservice.service;

import java.util.List;
import java.util.Optional;

import org.hibernate.ObjectNotFoundException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.unirio.equipmentservice.dto.BicicletaDTO;
import dev.unirio.equipmentservice.dto.BicicletaIntegracaoDTO;
import dev.unirio.equipmentservice.dto.BicicletaRequestDTO;
import dev.unirio.equipmentservice.entity.Bicicleta;
import dev.unirio.equipmentservice.entity.Tranca;
import dev.unirio.equipmentservice.enumeration.BicicletaStatus;
import dev.unirio.equipmentservice.exception.NegocioException;
import dev.unirio.equipmentservice.mapper.BicicletaMapper;
import dev.unirio.equipmentservice.repository.BicicletaRepository;
import dev.unirio.equipmentservice.service.implementation.BicicletaServiceImplementation;

@ExtendWith(MockitoExtension.class)
class BicicletaServiceTest {

    private static final Long ID = 1L;
    private static final Long ID_INVALIDO = 99L;

    @Mock
    private BicicletaRepository repository;

    @Mock
    private BicicletaMapper mapper;

    @Mock
    private TrancaService trancaService;

    @InjectMocks
    private BicicletaServiceImplementation service;

    private Bicicleta bicicleta;
    private BicicletaDTO bicicletaDTO;
    private BicicletaRequestDTO requestDTO;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        bicicleta = new Bicicleta();
        bicicleta.setId(ID);
        bicicleta.setStatus(BicicletaStatus.NOVA);

        bicicletaDTO = new BicicletaDTO();
        bicicletaDTO.setId(ID);

        requestDTO = new BicicletaRequestDTO(
            "Caloi", "Elite", "2024", 101, BicicletaStatus.NOVA
        );
    }

    /* =========================
       BUSCAR TODAS
       ========================= */

    @Test
    void buscarBicicletas() {
        when(repository.findAll()).thenReturn(List.of(bicicleta));
        when(mapper.toDtoList(any())).thenReturn(List.of(bicicletaDTO));

        List<BicicletaDTO> resultado = service.buscarBicicletas();

        assertEquals(1, resultado.size());
        verify(repository).findAll();
        verify(mapper).toDtoList(any());
    }

    /* =========================
       BUSCAR POR ID
       ========================= */

    @Test
    void buscarBicicleta() {
        when(repository.findById(ID)).thenReturn(Optional.of(bicicleta));
        when(mapper.toDto(bicicleta)).thenReturn(bicicletaDTO);

        BicicletaDTO resultado = service.buscarBicicleta(ID);

        assertNotNull(resultado);
        verify(repository).findById(ID);
    }

    @Test
    void buscarBicicletaIdNull() {
        Throwable exception = assertThrows(NegocioException.class,
            () -> service.buscarBicicleta(null));
        
        assertNotNull(exception);
    }

    @Test
    void buscarBicicletaNotFound() {
        when(repository.findById(ID_INVALIDO)).thenReturn(Optional.empty());

        Throwable exception = assertThrows(ObjectNotFoundException.class,
            () -> service.buscarBicicleta(ID_INVALIDO));
        
        assertNotNull(exception);
    }

    /* =========================
       CRIAR
       ========================= */

    @Test
    void criarBicicleta() {
        when(mapper.toEntity(requestDTO)).thenReturn(bicicleta);
        when(repository.save(bicicleta)).thenReturn(bicicleta);
        when(mapper.toDto(bicicleta)).thenReturn(bicicletaDTO);

        BicicletaDTO resultado = service.criarBicicleta(requestDTO);

        assertNotNull(resultado);
        verify(repository).save(bicicleta);
    }

    @Test
    void criarBicicletaStatusInvalido() {
        requestDTO.setStatus(BicicletaStatus.EM_USO);

        Throwable exception = assertThrows(NegocioException.class,
            () -> service.criarBicicleta(requestDTO));

        assertNotNull(exception);
    }

    @Test
    void criarBicicletaMapperNull() {
        when(mapper.toEntity(any())).thenReturn(null);

        Throwable exception = assertThrows(NegocioException.class,
            () -> service.criarBicicleta(requestDTO));

        assertNotNull(exception);
    }

    /* =========================
       ALTERAR STATUS
       ========================= */

     @Test
    void alterarStatusIdNull() {
        Throwable exception = assertThrows(NegocioException.class,
                () -> service.alterarStatus(null, BicicletaStatus.EM_USO));
    
        assertNotNull(exception);
    }

    @Test
    void alterarStatusNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Throwable exception = assertThrows(ObjectNotFoundException.class,
                () -> service.alterarStatus(1L, BicicletaStatus.EM_USO));

        assertNotNull(exception);
    }

    @Test
    void alterarStatusNegocioException() {
        bicicleta.setStatus(BicicletaStatus.REPARO_SOLICITADO);

        when(repository.findById(1L)).thenReturn(Optional.of(bicicleta));

        Throwable exception = assertThrows(NegocioException.class,
                () -> service.alterarStatus(1L, BicicletaStatus.EM_USO));

        assertNotNull(exception);
    }

    @Test
    void alterarStatus() {
        when(repository.findById(1L)).thenReturn(Optional.of(bicicleta));
        when(repository.save(bicicleta)).thenReturn(bicicleta);
        when(mapper.toDto(bicicleta)).thenReturn(bicicletaDTO);

        BicicletaDTO resultado =
                service.alterarStatus(1L, BicicletaStatus.EM_USO);

        assertNotNull(resultado);
        assertEquals(BicicletaStatus.EM_USO, bicicleta.getStatus());

        verify(repository).save(bicicleta);
        verify(mapper).toDto(bicicleta);
    }

    /* =========================
       INTEGRAR REDE
       ========================= */

    @Test
    void integrarRede() {
        BicicletaIntegracaoDTO dto = new BicicletaIntegracaoDTO();
        dto.setBicicleta(ID);
        dto.setTranca(10L);

        when(repository.findById(ID)).thenReturn(Optional.of(bicicleta));
        when(trancaService.buscarEntidade(10L)).thenReturn(new Tranca());

        service.integrarRede(dto);

        assertEquals(BicicletaStatus.DISPONIVEL, bicicleta.getStatus());
        verify(trancaService).trancar(10L, ID);
        verify(repository).save(bicicleta);
    }

    @Test
    void integrarRedeBicicletaNull() {
        BicicletaIntegracaoDTO dto = new BicicletaIntegracaoDTO();

        Throwable exception = assertThrows(
            NegocioException.class, 
            () -> service.integrarRede(dto));

        assertNotNull(exception);
    }

     @Test
    void integrarRedeTrancaNull() {
        BicicletaIntegracaoDTO dto = new BicicletaIntegracaoDTO();
        dto.setBicicleta(ID);
        dto.setTranca(null);

        Throwable exception = assertThrows(
            NegocioException.class, 
            () -> service.integrarRede(dto));

        assertNotNull(exception);
    }

    @Test
    void integrarRedeNotFound() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        BicicletaIntegracaoDTO dto = new BicicletaIntegracaoDTO();
        dto.setBicicleta(ID);
        dto.setTranca(ID);

        Throwable exception = assertThrows(
            ObjectNotFoundException.class,
            () -> service.integrarRede(dto));

        assertNotNull(exception);
    }

    @Test
    void integrarRedeBicicletaComTranca() {
        
        bicicleta.setTranca(new Tranca());

        BicicletaIntegracaoDTO dto = new BicicletaIntegracaoDTO();
        dto.setBicicleta(ID);
        dto.setTranca(ID);

        when(repository.findById(ID)).thenReturn(Optional.of(bicicleta));
        
        Throwable exception = assertThrows(
            NegocioException.class, 
            () -> service.integrarRede(dto));

        assertNotNull(exception);
    }

    @Test
    void integrarRedeStatusInvalido() {
        bicicleta.setStatus(BicicletaStatus.DISPONIVEL);

        BicicletaIntegracaoDTO dto = new BicicletaIntegracaoDTO();
        dto.setBicicleta(ID);
        dto.setTranca(ID);

        when(repository.findById(ID)).thenReturn(Optional.of(bicicleta));

        Throwable exception = assertThrows(
            NegocioException.class,
            () -> service.integrarRede(dto));

        assertNotNull(exception);
    }

    /* =========================
       RETIRAR DA REDE
       ========================= */

    @Test
    void retirarRede() {
        Tranca tranca = new Tranca();
        tranca.setId(10L);

        bicicleta.setTranca(tranca);
        bicicleta.setStatus(BicicletaStatus.REPARO_SOLICITADO);

        BicicletaIntegracaoDTO dto = new BicicletaIntegracaoDTO();
        dto.setBicicleta(ID);
        dto.setStatus(BicicletaStatus.EM_REPARO);

        when(repository.findById(ID)).thenReturn(Optional.of(bicicleta));

        service.retirarRede(dto);

        assertNull(bicicleta.getTranca());
        assertEquals(BicicletaStatus.EM_REPARO, bicicleta.getStatus());
        verify(trancaService).destrancar(10L, ID);
        verify(repository).save(bicicleta);
    }

    @Test
    void retirarRedeStatusInvalido() {
        BicicletaIntegracaoDTO dto = new BicicletaIntegracaoDTO();
        dto.setBicicleta(ID);
        dto.setStatus(BicicletaStatus.DISPONIVEL);

        Throwable exception = assertThrows(NegocioException.class,
            () -> service.retirarRede(dto));

        assertNotNull(exception);
    }

    @Test
    void retirarRedeBicicletaNull(){
        BicicletaIntegracaoDTO dto = new BicicletaIntegracaoDTO();
        
        Throwable exception = assertThrows(NegocioException.class,
            () -> service.retirarRede(dto));

        assertNotNull(exception);
    }

    @Test
    void retirarRedeNotFound() {
        BicicletaIntegracaoDTO dto = new BicicletaIntegracaoDTO();
        dto.setBicicleta(ID);
        dto.setStatus(BicicletaStatus.APOSENTADA);

        when(repository.findById(ID)).thenReturn(Optional.empty());
        
        Throwable exception = assertThrows(ObjectNotFoundException.class,
            () -> service.retirarRede(dto));

        assertNotNull(exception);
    }

    @Test
    void retirarRedeBicicletaSemTranca() {
        BicicletaIntegracaoDTO dto = new BicicletaIntegracaoDTO();
        dto.setBicicleta(ID);
        dto.setStatus(BicicletaStatus.EM_REPARO);
        bicicleta.setTranca(null);

        when(repository.findById(ID)).thenReturn(Optional.of(bicicleta));
        
        Throwable exception = assertThrows(
            NegocioException.class,
            () -> service.retirarRede(dto));

        assertNotNull(exception);
    }

    @Test
    void retirarRedeBicicletaEmUso() {
        BicicletaIntegracaoDTO dto = new BicicletaIntegracaoDTO();
        dto.setBicicleta(ID);
        dto.setStatus(BicicletaStatus.APOSENTADA);

        bicicleta.setTranca(new Tranca());
        bicicleta.setStatus(BicicletaStatus.EM_USO);
        
        when(repository.findById(ID)).thenReturn(Optional.of(bicicleta));
        
        Throwable exception = assertThrows(
            NegocioException.class,
            () -> service.retirarRede(dto));

        assertNotNull(exception);
    }

    @Test
    void retirarRedeReparoNaoSolicitado() {
        BicicletaIntegracaoDTO dto = new BicicletaIntegracaoDTO();
        dto.setBicicleta(ID);
        dto.setStatus(BicicletaStatus.EM_REPARO);

        bicicleta.setTranca(new Tranca());
        bicicleta.setStatus(BicicletaStatus.EM_REPARO);
        
        when(repository.findById(ID)).thenReturn(Optional.of(bicicleta));
        
        Throwable exception = assertThrows(
            NegocioException.class,
            () -> service.retirarRede(dto));

        assertNotNull(exception);
    }

    /* =========================
       ATUALIZAR
       ========================= */

    @Test
    void atualizarBicicleta() {
        when(repository.findById(ID)).thenReturn(Optional.of(bicicleta));
        when(repository.save(bicicleta)).thenReturn(bicicleta);
        when(mapper.toDto(bicicleta)).thenReturn(bicicletaDTO);

        BicicletaDTO dto = service.atualizarBicicleta(ID, requestDTO);

        assertNotNull(dto);
        verify(mapper).updateEntityFromDto(requestDTO, bicicleta);
    }

    @Test
    void atualizarBicicletaIdNull() {
        Throwable exception = assertThrows(
            NegocioException.class,
            () -> service.atualizarBicicleta(null, requestDTO));

        assertNotNull(exception);
    }

    @Test
    void atualizarBicicletaNotFound() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        Throwable exception = assertThrows(
            ObjectNotFoundException.class,
            () -> service.atualizarBicicleta(ID, requestDTO));

        assertNotNull(exception);
    }

    /* =========================
       DELETAR
       ========================= */

    @Test
    void deletarBicicleta() {
        when(repository.existsById(ID)).thenReturn(true);
        service.deletarBicicleta(ID);
        verify(repository).deleteById(ID);
    }

    @Test
    void deletarBicicletaNotFound() {
        when(repository.existsById(ID)).thenReturn(false);
        Throwable exception = assertThrows(
            ObjectNotFoundException.class,
            () -> service.deletarBicicleta(ID));

        assertNotNull(exception);
    }

    @Test
    void deletarBicicletaIdNull() {
        Throwable exception = assertThrows(NegocioException.class,
            () -> service.deletarBicicleta(null));

        assertNotNull(exception);
    }
}
