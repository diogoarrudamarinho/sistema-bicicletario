package dev.unirio.equipmentservice.service;

import java.util.ArrayList;
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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
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
import dev.unirio.equipmentservice.mapper.TrancaMapper;
import dev.unirio.equipmentservice.repository.TrancaRepository;
import dev.unirio.equipmentservice.service.implementation.TrancaServiceImplementation;

@ExtendWith(MockitoExtension.class)
class TrancaServiceTest {
    
    @InjectMocks
    private TrancaServiceImplementation service;

    @Mock
    private TrancaRepository repository;

    @Mock
    private TrancaMapper mapper;

    @Mock
    private BicicletaService bicicletaService;

    @Mock   
    private TotemService totemService;

    private Tranca tranca;
    private static final Long ID = 1L;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        tranca = new Tranca();
        tranca.setId(ID);
        tranca.setStatus(TrancaStatus.LIVRE);
    }

    @Test
    void testBuscarTrancas(){
        List<TrancaDTO> trancas = new ArrayList<>();
        
        when(repository.findAll()).thenReturn(new ArrayList<>());
        when(mapper.toDtoList(anyList())).thenReturn(trancas);

        List<TrancaDTO> request = service.buscarTrancas();

        assertEquals(trancas, request);
        assertNotNull(request);
    }

    /* BUSCAR TRANCA */

    @Test
    void testBuscarTranca(){
        TrancaDTO trancaDto = new TrancaDTO();
        trancaDto.setId(ID);

        when(repository.findById(ID)).thenReturn(Optional.of(tranca));
        when(mapper.toDto(tranca)).thenReturn(trancaDto);

        TrancaDTO resultado = service.buscarTranca(ID);

        assertNotNull(resultado);
        assertEquals(ID, resultado.getId());
        verify(repository).findById(ID);
        verify(mapper).toDto(tranca);
    }

    @Test
    void testBuscarTrancaIdNull() {
        Throwable exception = assertThrows(NegocioException.class, () -> service.buscarTranca(null));
        assertNotNull(exception);
    }

    @Test
    void testBuscarTrancaNaoEncontrada() {
        when(repository.findById(ID)).thenReturn(Optional.empty());
        Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.buscarTranca(ID));
        assertNotNull(exception);
    }

    /* Buscar Bicicleta */
    @Test
    void testBuscarBicicleta() {
        Bicicleta bike = new Bicicleta();
        bike.setId(10L);
        tranca.setBicicleta(bike);
        
        when(repository.findById(ID)).thenReturn(Optional.of(tranca));
        when(bicicletaService.buscarBicicleta(10L)).thenReturn(new BicicletaDTO());

        assertNotNull(service.buscarBicicleta(ID));
    }

    @Test
    void testBuscarBicicletaNotFound(){
        when(repository.findById(ID)).thenReturn(Optional.empty());
        Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.buscarBicicleta(ID));
        assertNotNull(exception);
    }

    @Test
    void testBuscarBicicletaIdNull(){
         Throwable exception = assertThrows(NegocioException.class, () -> service.buscarBicicleta(null));
        assertNotNull(exception);
    }

    /* Cadastrar Tranca */

    @Test
    void testCadastrarTranca(){

        TrancaDTO t = new TrancaDTO();

        when(mapper.toEntity(any(TrancaRequestDTO.class)))
        .thenReturn(tranca);

        when(mapper.toDto(tranca))
        .thenReturn(t);

        when(repository.save(any(Tranca.class)))
        .thenReturn(tranca);

        TrancaDTO request = service.cadastrarTranca(new TrancaRequestDTO());

        assertEquals(t, request);
    }

    @Test
    void testCadastrarTrancaNull() {
        TrancaRequestDTO request = new TrancaRequestDTO(); 
        when(mapper.toEntity(any())).thenReturn(null);

        
        NegocioException exception = assertThrows(NegocioException.class, () -> service.cadastrarTranca(request));
        
        assertNotNull(exception);
    }

    /* TRANCAR */
    
    @Test
    void testTrancar() {

        TrancaDTO t = new TrancaDTO();
        Bicicleta b = new Bicicleta();

        when(repository.findById(ID))
        .thenReturn(Optional.of(tranca));

        when(repository.save(tranca))
        .thenReturn(tranca);

        when(mapper.toDto(tranca))
        .thenReturn(t);

        when(bicicletaService.alterarStatus(anyLong(), any(BicicletaStatus.class)))
        .thenReturn(new BicicletaDTO());

        when(bicicletaService.buscarEntidade(anyLong()))
        .thenReturn(b);

        TrancaDTO request = service.trancar(ID, ID);

        assertEquals(t, request);
        assertEquals(b, tranca.getBicicleta());
        assertEquals(TrancaStatus.OCUPADA, tranca.getStatus());
        verify(bicicletaService).alterarStatus(anyLong(), any(BicicletaStatus.class));
        verify(bicicletaService).buscarEntidade(anyLong());
    }

    @Test
    void trancarBicicletaNull(){

        TrancaDTO t = new TrancaDTO();

        when(repository.findById(ID))
        .thenReturn(Optional.of(tranca));

        when(repository.save(tranca))
        .thenReturn(tranca);

        when(mapper.toDto(tranca))
        .thenReturn(t);

        TrancaDTO request = service.trancar(ID, null);

        assertEquals(t, request);
        assertEquals(TrancaStatus.OCUPADA, tranca.getStatus());
        verify(bicicletaService, never()).alterarStatus(anyLong(), any(BicicletaStatus.class));
        verify(bicicletaService, never()).buscarEntidade(anyLong());
    }

    @Test
    void testTrancarIdNull(){
        Throwable exception = assertThrows(NegocioException.class, () -> service.trancar(null, 1L));
        assertNotNull(exception);
    }

    @Test
    void testTrancarException() {
        when(repository.findById(ID)).thenReturn(Optional.empty());
        Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.trancar(ID, ID));
        assertNotNull(exception);
    }

    /* DESTRANCAR */

    @Test
    void testDestrancar() {

        TrancaDTO t = new TrancaDTO();

        when(repository.findById(ID))
        .thenReturn(Optional.of(tranca));

        when(repository.save(tranca))
        .thenReturn(tranca);

        when(mapper.toDto(tranca))
        .thenReturn(t);

        when(bicicletaService.alterarStatus(anyLong(), any(BicicletaStatus.class)))
        .thenReturn(new BicicletaDTO());

        TrancaDTO request = service.destrancar(ID, ID);

        assertEquals(t, request);
        assertEquals(TrancaStatus.LIVRE, tranca.getStatus());
        assertNull(tranca.getBicicleta());
        verify(bicicletaService).alterarStatus(anyLong(), any(BicicletaStatus.class));
    }

    @Test
    void testDestrancarBicicletaNull() {
        TrancaDTO t = new TrancaDTO();

        when(repository.findById(ID))
        .thenReturn(Optional.of(tranca));

        when(repository.save(tranca))
        .thenReturn(tranca);

        when(mapper.toDto(tranca))
        .thenReturn(t);

        TrancaDTO request = service.destrancar(ID, null);

        assertEquals(t, request);
        assertEquals(TrancaStatus.LIVRE, tranca.getStatus());
        verify(bicicletaService, never()).alterarStatus(anyLong(), any(BicicletaStatus.class));
    }

    @Test
    void testDestrancarIdNull() {
        NegocioException exception = assertThrows(NegocioException.class, () -> service.destrancar(null, 1L));
        
        assertNotNull(exception);
    }

    @Test
    void testDestrancarException() {
        when(repository.findById(ID)).thenReturn(Optional.empty());
        Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.destrancar(ID, ID));
        assertNotNull(exception);
    }

    /* INTEGRAR REDE */

    @Test
    void testIntegrarRede() {
        Totem totem = new Totem();
        TrancaIntegracaoDTO request = new TrancaIntegracaoDTO();
        request.setTotem(1L);
        request.setTranca(ID);

        when(repository.findById(anyLong()))
        .thenReturn(Optional.of(tranca));

        when(totemService.buscarEntidade(anyLong()))
        .thenReturn(totem);

        service.integrarRede(request);

        assertEquals(totem, tranca.getTotem());
        verify(totemService).buscarEntidade(anyLong());
        verify(repository).save(any(Tranca.class));
    }

    @Test
    void testIntegrarRedeNotFound(){
        TrancaIntegracaoDTO request =  new TrancaIntegracaoDTO();
        request.setTotem(1L);
        request.setTranca(ID);

        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.integrarRede(request));
        assertNotNull(exception);
    }

    @Test
    void testIntegrarRedeTotemNull(){
        TrancaIntegracaoDTO request =  new TrancaIntegracaoDTO();
        request.setTotem(null);

        Throwable exception = assertThrows(NegocioException.class, () -> service.integrarRede(request));
        assertNotNull(exception);
    }

    /* RETIRAR REDE */

     @Test
    void testRetirarRede() {
        TrancaIntegracaoDTO request = new TrancaIntegracaoDTO();
        request.setStatus(TrancaStatus.APOSENTADA);
        request.setTranca(ID);

        when(repository.findById(anyLong()))
        .thenReturn(Optional.of(tranca));
        service.retirarRede(request);

        assertEquals(TrancaStatus.APOSENTADA, tranca.getStatus());
        assertNull(tranca.getTotem());
        verify(repository).save(any(Tranca.class));
    }

    @Test
    void testRetirarRedeNotFound(){
        TrancaIntegracaoDTO request =  new TrancaIntegracaoDTO();
        request.setStatus(TrancaStatus.APOSENTADA);
        request.setTranca(ID);

        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.retirarRede(request));
        assertNotNull(exception);
    }

    @Test
    void testRetirarRedeStatusOcupado(){
        TrancaIntegracaoDTO request = new TrancaIntegracaoDTO();

        request.setTranca(ID);
        request.setStatus(TrancaStatus.LIVRE);
        tranca.setStatus(TrancaStatus.OCUPADA); 

        when(repository.findById(ID)).thenReturn(Optional.of(tranca));

        NegocioException exception = assertThrows(NegocioException.class, 
            () -> service.retirarRede(request));
        
        assertEquals("Tranca está ocupada", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void testRetirarRedeStatusNull(){
        TrancaIntegracaoDTO request =  new TrancaIntegracaoDTO();
        request.setStatus(null);
        Throwable exception = assertThrows(NegocioException.class, () -> service.retirarRede(request));
        assertNotNull(exception);
    }

    /* ATUALIZAR TRANCA */

    @Test
    void testAtualizarTranca(){
        TrancaRequestDTO trancaRequest = new TrancaRequestDTO();
        TrancaDTO trancaDto = new TrancaDTO();
        trancaDto.setId(ID);

        when(repository.findById(anyLong())).thenReturn(Optional.of(tranca));
        doNothing().when(mapper).updateEntityFromDto(trancaRequest, tranca);
        when(repository.save(tranca)).thenReturn(tranca);
        when(mapper.toDto(tranca)).thenReturn(trancaDto);

        TrancaDTO request = service.atualizarTranca(ID, trancaRequest);

        assertEquals(trancaDto, request);
    }

    @Test
    void testAtualizarTrancaNotFound(){
        TrancaRequestDTO trancaRequest = new TrancaRequestDTO();

        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.atualizarTranca(1L, trancaRequest));
        assertNotNull(exception);
    }

    @Test
    void testAtualizarTrancaTrancaRequestNull(){
         Throwable exception = assertThrows(NegocioException.class, () -> service.atualizarTranca(1L, null));
        assertNotNull(exception);
    }
    
    @Test
    void testAtualizarTrancaIdNull(){
        TrancaRequestDTO trancaRequest = new TrancaRequestDTO();
        Throwable exception = assertThrows(NegocioException.class, () -> service.atualizarTranca(null, trancaRequest));
        assertNotNull(exception);
    }

    /* DELETAR */

    @Test
    void testDeletarIdNull(){
        Throwable exception = assertThrows(NegocioException.class, () -> service.deletar(null));
        assertNotNull(exception);
    }

    @Test
    void testDeletarNotExists(){
        when(repository.existsById(ID)).thenReturn(false);

        Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.deletar(ID));
        assertNotNull(exception);
    }

    @Test
    void testDeletar(){
        when(repository.existsById(ID)).thenReturn(true);
        
        service.deletar(ID);

        verify(repository).deleteById(ID);
    }

    /* ALTERAR STATUS */

    @Test
    void testAlterarStatusSucessoAposentada(){
        TrancaDTO tdto = new TrancaDTO();
        tdto.setStatus(TrancaStatus.APOSENTADA);
        tranca.setStatus(TrancaStatus.LIVRE);

        when(repository.findById(anyLong())).thenReturn(Optional.of(tranca));
        when(mapper.toDto(tranca)).thenReturn(tdto);
        when(repository.save(tranca)).thenReturn(tranca);

        TrancaDTO request = service.alterarStatus(ID, TrancaStatus.APOSENTADA);

        assertEquals(TrancaStatus.APOSENTADA, tranca.getStatus());
        assertNotNull(request);
    }

    @Test
    void testAlterarStatusSucessoEmReparo(){
        TrancaDTO tdto = new TrancaDTO();
        tdto.setStatus(TrancaStatus.EM_REPARO);
        tranca.setStatus(TrancaStatus.LIVRE);

        when(repository.findById(anyLong())).thenReturn(Optional.of(tranca));
        when(mapper.toDto(tranca)).thenReturn(tdto);
        when(repository.save(tranca)).thenReturn(tranca);

        TrancaDTO request = service.alterarStatus(ID, TrancaStatus.EM_REPARO);

        assertEquals(TrancaStatus.EM_REPARO, tranca.getStatus());
        assertNotNull(request);
    }

    @Test
    void testAlterarStatusSucessoOcupada(){
        TrancaDTO tdto = new TrancaDTO();
        tdto.setStatus(TrancaStatus.REPARO_SOLICITADO);
        tranca.setStatus(TrancaStatus.OCUPADA);

        when(repository.findById(anyLong())).thenReturn(Optional.of(tranca));
        when(mapper.toDto(tranca)).thenReturn(tdto);
        when(repository.save(tranca)).thenReturn(tranca);

        TrancaDTO request = service.alterarStatus(ID, TrancaStatus.REPARO_SOLICITADO);

        assertEquals(TrancaStatus.REPARO_SOLICITADO, tranca.getStatus());
        assertNotNull(request);
    }

    @Test
    void testAlterarStatusEmReparo(){
        tranca.setStatus(TrancaStatus.OCUPADA);

        when(repository.findById(anyLong())).thenReturn(Optional.of(tranca));
        Throwable exception = assertThrows(NegocioException.class, () -> service.alterarStatus(1L, TrancaStatus.EM_REPARO));
        assertNotNull(exception);
    }

    @Test
    void testAlterarStatusAposentar(){
        tranca.setStatus(TrancaStatus.OCUPADA);

        when(repository.findById(anyLong())).thenReturn(Optional.of(tranca));
        Throwable exception = assertThrows(NegocioException.class, () -> service.alterarStatus(1L, TrancaStatus.APOSENTADA));
        assertNotNull(exception);
    }

    @Test
    void testAlterarStatusIdNull(){
        Throwable exception = assertThrows(NegocioException.class, () -> service.alterarStatus(null, null));
        assertNotNull(exception);
    }

    @Test
    void testAlterarStatusLivre(){
        Throwable exception = assertThrows(NegocioException.class, () -> service.alterarStatus(1L, TrancaStatus.LIVRE));
        assertNotNull(exception);
    }

    @Test
    void testAlterarStatusOcupada(){
        Throwable exception = assertThrows(NegocioException.class, () -> service.alterarStatus(1L, TrancaStatus.OCUPADA));
        assertNotNull(exception);
    }

    @Test
    void testAlterarStatusNotFound(){
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.alterarStatus(1L, TrancaStatus.NOVA));
        assertNotNull(exception);
    }
}
