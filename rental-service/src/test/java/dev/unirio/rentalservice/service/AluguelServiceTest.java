package dev.unirio.rentalservice.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import dev.unirio.rentalservice.client.EquipmentClient;
import dev.unirio.rentalservice.client.ExternalClient;
import dev.unirio.rentalservice.dto.AluguelDTO;
import dev.unirio.rentalservice.dto.AluguelRequestDTO;
import dev.unirio.rentalservice.dto.BicicletaDTO;
import dev.unirio.rentalservice.dto.CiclistaDTO;
import dev.unirio.rentalservice.dto.CobrancaResponseDTO;
import dev.unirio.rentalservice.entity.Aluguel;
import dev.unirio.rentalservice.enumeration.BicicletaStatus;
import dev.unirio.rentalservice.enumeration.StatusCobranca;
import dev.unirio.rentalservice.mapper.AluguelMapper;
import dev.unirio.rentalservice.repository.AluguelRepository;
import dev.unirio.rentalservice.service.implementation.AluguelServiceImplementation;

@ExtendWith(MockitoExtension.class)
class AluguelServiceTest {
    
    @Mock private AluguelRepository repository;
    @Mock private EquipmentClient equipmentClient;
    @Mock private ExternalClient externalClient;
    @Mock private CiclistaService ciclistaService;
    @Mock private AluguelMapper mapper;

    @InjectMocks
    private AluguelServiceImplementation service;

    private AluguelRequestDTO requestDTO;
    private CiclistaDTO ciclistaDTO;
    private BicicletaDTO bicicletaDTO;
    private CobrancaResponseDTO cobrancaDTO;
    private Aluguel aluguel;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "baseValue", new BigDecimal("5.00"));

        requestDTO = new AluguelRequestDTO(1L, 10L); 
        ciclistaDTO = new CiclistaDTO(1L, "Diogo", "user@email.com", "11111111111", null, null, null, null, null);
        bicicletaDTO = new BicicletaDTO(1L, null, "Caloi", null, null, BicicletaStatus.DISPONIVEL);
        cobrancaDTO = new CobrancaResponseDTO(500L, new BigDecimal(5.0), 1L, StatusCobranca.PAGA, LocalDateTime.now(), LocalDateTime.now());
        
        aluguel = new Aluguel(1L, 1L, 10L, 500L);
    }

    // --- TESTES DA FUNÇÃO: alugar ---

    @Test
    void alugar() {
        when(ciclistaService.buscarCiclista(1L)).thenReturn(ciclistaDTO);
        when(repository.existsByCiclistaAndHoraFimIsNull(1L)).thenReturn(false);
        when(equipmentClient.getBicicletaByTranca(10L)).thenReturn(bicicletaDTO);
        when(externalClient.postRealizaCobranca(any())).thenReturn(cobrancaDTO);
        when(repository.save(any(Aluguel.class))).thenReturn(aluguel);
        when(mapper.toDto(any())).thenReturn(new AluguelDTO(1L, 1L, 1L, 10L, 2L, null, null, 500L));

        AluguelDTO resultado = service.alugar(requestDTO);

        assertNotNull(resultado);
        verify(equipmentClient).postDestrancar(10L, 1L);
        verify(repository).save(any(Aluguel.class));
    }

    @Test
    void alugarCiclistaComAluguelAtivo() {
        when(ciclistaService.buscarCiclista(1L)).thenReturn(ciclistaDTO);
        when(repository.existsByCiclistaAndHoraFimIsNull(1L)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.alugar(requestDTO));
        assertNotNull(exception);
        verify(externalClient, never()).postRealizaCobranca(any());
    }

    @Test
    void alugarErroAoDestrancar() {
        when(ciclistaService.buscarCiclista(1L)).thenReturn(ciclistaDTO);
        when(repository.existsByCiclistaAndHoraFimIsNull(1L)).thenReturn(false);
        when(equipmentClient.getBicicletaByTranca(10L)).thenReturn(bicicletaDTO);
        when(externalClient.postRealizaCobranca(any())).thenReturn(cobrancaDTO);

        doThrow(new RuntimeException()).when(equipmentClient).postDestrancar(any(), any());

        assertThrows(RuntimeException.class, () -> service.alugar(requestDTO));

        verify(externalClient).postEstornarCobranca(500L);
        verify(equipmentClient).postAlterarStatusBicicleta(1L, BicicletaStatus.DISPONIVEL);
    }

    // --- TESTES DA FUNÇÃO: devolver ---

    @Test
    void devolver() {
        Aluguel aluguelSpy = spy(aluguel);
        when(repository.findByCiclistaAndHoraFimIsNull(1L)).thenReturn(Optional.of(aluguelSpy));
        when(equipmentClient.getBicicletaByTranca(10L)).thenReturn(bicicletaDTO);
        when(aluguelSpy.calcularValorExcedente()).thenReturn(BigDecimal.ZERO);
        when(repository.save(any())).thenReturn(aluguelSpy);
        when(mapper.toDto(any())).thenReturn(new AluguelDTO(1L, 1L, 1L, 10L, 2L, null, null, 500L));

        AluguelDTO resultado = service.devolver(requestDTO);

        assertNotNull(resultado);
        verify(externalClient, never()).postRealizaCobranca(any());
        verify(equipmentClient).postDestrancar(10L, 1L);
    }

    @Test
    void devolverComValorExcedente() {
        Aluguel aluguelSpy = spy(aluguel);
        when(repository.findByCiclistaAndHoraFimIsNull(1L)).thenReturn(Optional.of(aluguelSpy));
        when(equipmentClient.getBicicletaByTranca(10L)).thenReturn(bicicletaDTO);
        
        when(aluguelSpy.calcularValorExcedente()).thenReturn(new BigDecimal("5.00"));
        when(externalClient.postRealizaCobranca(any())).thenReturn(cobrancaDTO);
        
        when(repository.save(any())).thenReturn(aluguelSpy);
        when(mapper.toDto(any())).thenReturn(new AluguelDTO(1L, 1L, 1L, 10L, 2L, null, null, 500L));

        service.devolver(requestDTO);

        verify(externalClient).postRealizaCobranca(any());
    }

    @Test
    void devolverAluguelNaoEncontrado() {
        when(repository.findByCiclistaAndHoraFimIsNull(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.devolver(requestDTO));
    }

    @Test
    void devolverErroAoProcessarDevolucaoComEstorno() {
        Aluguel aluguelSpy = spy(aluguel);
        when(repository.findByCiclistaAndHoraFimIsNull(1L)).thenReturn(Optional.of(aluguelSpy));
        when(equipmentClient.getBicicletaByTranca(10L)).thenReturn(bicicletaDTO);
        
        when(aluguelSpy.calcularValorExcedente()).thenReturn(new BigDecimal("5.00"));
        when(externalClient.postRealizaCobranca(any())).thenReturn(cobrancaDTO);

        doThrow(new RuntimeException()).when(equipmentClient).postDestrancar(any(), any());

        assertThrows(RuntimeException.class, () -> service.devolver(requestDTO));

        verify(externalClient).postEstornarCobranca(500L);
    }
}
