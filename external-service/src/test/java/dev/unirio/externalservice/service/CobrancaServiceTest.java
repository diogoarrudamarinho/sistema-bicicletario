package dev.unirio.externalservice.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.unirio.externalservice.client.paypal.PaypalClient;
import dev.unirio.externalservice.client.paypal.cartao.CartaoClient;
import dev.unirio.externalservice.dto.CartaoDTO;
import dev.unirio.externalservice.dto.CobrancaDTO;
import dev.unirio.externalservice.dto.CobrancaRequestDTO;
import dev.unirio.externalservice.entity.Cobranca;
import dev.unirio.externalservice.enumeration.StatusCobranca;
import dev.unirio.externalservice.exception.ObjectNotFoundException;
import dev.unirio.externalservice.exception.cobranca.PaypalApiException;
import dev.unirio.externalservice.exception.cobranca.PaypalApiException.PaypalErrorDetail;
import dev.unirio.externalservice.mapper.CobrancaMapper;
import dev.unirio.externalservice.repository.CobrancaRepository;
import dev.unirio.externalservice.service.implementation.CobrancaServiceImplementation;

@ExtendWith(MockitoExtension.class)
class CobrancaServiceTest {
    @InjectMocks
    private CobrancaServiceImplementation service;

    @Mock
    private CobrancaRepository repository;

    @Mock
    private PaypalClient paypalClient;

    @Mock
    private CartaoClient cartaoClient;

    @Mock
    private CobrancaMapper mapper;

    private CartaoDTO cartao;
    private CobrancaRequestDTO request;
    private Cobranca cobranca;
    private CobrancaDTO cobrancaDTO;

    @BeforeEach
    @SuppressWarnings("unused") 
    void setup() {
        cartao = new CartaoDTO("Titular",
                                "4111111111111111",
                                "2030-12",
                                "123");

        request = new CobrancaRequestDTO();
        request.setCiclista(1L);
        request.setValor(new BigDecimal("100.00"));

        cobranca = new Cobranca();
        cobranca.setId(10L);
        cobranca.setValor(new BigDecimal("100.00"));
        cobranca.setCiclista(1L);
        cobranca.setStatus(StatusCobranca.PAGA);
        cobranca.setHoraFinalizacao(LocalDateTime.now());

        cobrancaDTO = new CobrancaDTO(
                        cobranca.getId(), 
                        cobranca.getValor(),
                        cobranca.getCiclista(),
                        cobranca.getStatus(),
                        cobranca.getHoraSolicitacao(), 
                        cobranca.getHoraFinalizacao());
    }

    // Método auxiliar para criar uma Cobrança com status FALHA para a fila
    private Cobranca createFilaCobranca() {
        Cobranca c = new Cobranca();
        c.setCiclista(1L);
        c.setValor(BigDecimal.ONE);
        c.setStatus(StatusCobranca.FALHA);
        return c;
    }
    
    @Test
    @SuppressWarnings("null")
    @DisplayName("Deve criar cobrança com sucesso quando paypal autoriza")
    void testCriarCobrancaSucesso() {
        when(cartaoClient.buscarCartao(request.getCiclista())).thenReturn(cartao);
        doNothing().when(paypalClient).autorizarTransacao(cartao, request.getValor());

        when(repository.save(any(Cobranca.class))).thenReturn(cobranca);
        when(mapper.toDTO(any(Cobranca.class))).thenReturn(cobrancaDTO);

        CobrancaDTO dto = service.criarCobranca(request);

        assertNotNull(dto);
        assertEquals(cobrancaDTO.getId(), dto.getId());
        assertEquals(StatusCobranca.PAGA, dto.getStatus());
        
        verify(repository).save(any(Cobranca.class));
        verify(cartaoClient).buscarCartao(request.getCiclista());
        verify(paypalClient).autorizarTransacao(cartao, request.getValor());
        verify(mapper).toDTO(any(Cobranca.class));
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Deve salvar cobrança como falha")
    void testCriarCobrancaErroCartao() {
        when(cartaoClient.buscarCartao(any())).thenReturn(cartao);

        PaypalErrorDetail detail = mock(PaypalErrorDetail.class);
        when(detail.getIssue()).thenReturn("CARD_DECLINED");

        PaypalApiException exception = new PaypalApiException(
            422, "UNPROCESSABLE_ENTITY", Collections.singletonList(detail)
        );

        doThrow(exception)
            .when(paypalClient)
            .autorizarTransacao(cartao, request.getValor());

        cobranca.setStatus(StatusCobranca.FALHA);
        when(repository.save(any(Cobranca.class))).thenReturn(cobranca);
        when(mapper.toDTO(any(Cobranca.class))).thenReturn(cobrancaDTO);

        CobrancaDTO dto = service.criarCobranca(request);

        assertNotNull(dto);
        assertEquals(cobrancaDTO.getId(), dto.getId());
        assertEquals(StatusCobranca.FALHA, cobranca.getStatus());

        verify(repository).save(any(Cobranca.class));
        verify(mapper).toDTO(any(Cobranca.class));

        verifyNoMoreInteractions(paypalClient);
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Deve relançar a exceção se PayPal retornar erro crítico")
    void testCriarCobrancaErroInterno() {
        when(cartaoClient.buscarCartao(any())).thenReturn(cartao);
        
        PaypalApiException ex = new PaypalApiException(
            500,
            "ERROR",
             Collections.emptyList());

        doThrow(ex).when(paypalClient).autorizarTransacao(any(), any());

        assertThrows(PaypalApiException.class, () -> service.criarCobranca(request));
        
        verify(repository, never()).save(any(Cobranca.class)); 
    }

    @Test
    @DisplayName("Deve retornar lista vazia se não houver cobranças com status FALHA")
    void testProcessarFilaCobrancaNull() {
        when(repository.findAllByStatus(StatusCobranca.FALHA)).thenReturn(Collections.emptyList());

        List<CobrancaDTO> result = service.processarFilaCobranca();

        assertTrue(result.isEmpty());
        verify(repository).findAllByStatus(StatusCobranca.FALHA);
        verifyNoMoreInteractions(cartaoClient, paypalClient, repository, mapper);
    }
    
    @SuppressWarnings("null")
    @Test
    @DisplayName("Deve pular e retornar lista vazia se CartaoClient retornar null")
    void testProcessarFilaCobrancaCartaoNulo() {
        Cobranca c1 = createFilaCobranca();
        when(repository.findAllByStatus(StatusCobranca.FALHA)).thenReturn(List.of(c1));
        
        when(cartaoClient.buscarCartao(c1.getCiclista())).thenReturn(null);

        List<CobrancaDTO> result = service.processarFilaCobranca();

        assertTrue(result.isEmpty());

        verify(cartaoClient).buscarCartao(c1.getCiclista());
        verify(repository, never()).save(any(Cobranca.class));
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Deve processar e salvar como PAGA se PayPal autorizar")
    void testProcessarFilaCobranca() {
        Cobranca c1 = createFilaCobranca();
        when(repository.findAllByStatus(StatusCobranca.FALHA)).thenReturn(List.of(c1));
        when(cartaoClient.buscarCartao(any())).thenReturn(cartao);
        
        doNothing().when(paypalClient).autorizarTransacao(any(), any());
        
        when(repository.save(any(Cobranca.class))).thenReturn(cobranca);
        when(mapper.toDTO(any(Cobranca.class))).thenReturn(cobrancaDTO);

        List<CobrancaDTO> result = service.processarFilaCobranca();

        assertFalse(result.isEmpty());
        
        verify(repository).save(argThat(c -> c.getStatus() == StatusCobranca.PAGA && c.getHoraFinalizacao() != null));
        verify(mapper).toDTO(any(Cobranca.class));
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Deve pular e retornar lista vazia se PayPal retornar erro de CARTAO")
    void testProcessarFilaCobrancaErroCartao() {
        Cobranca c1 = createFilaCobranca();
        when(repository.findAllByStatus(StatusCobranca.FALHA)).thenReturn(List.of(c1));
        when(cartaoClient.buscarCartao(any())).thenReturn(cartao);
        
        PaypalApiException ex = new PaypalApiException(
            422,
             "CARD_DECLINED",
            List.of(new PaypalErrorDetail("CARD_DECLINED", "Card rejected")));
            
        doThrow(ex).when(paypalClient).autorizarTransacao(any(), any());

        List<CobrancaDTO> result = service.processarFilaCobranca();

        assertTrue(result.isEmpty(), "A cobrança falhada deve ser ignorada");
        verify(paypalClient).autorizarTransacao(any(), any());
        verify(repository, never()).save(any(Cobranca.class)); 
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Deve relançar a exceção se PayPal retornar erro crítico")
    void testProcessarFilaCobrancaErroInterno() {
        Cobranca c1 = createFilaCobranca();
        when(repository.findAllByStatus(StatusCobranca.FALHA)).thenReturn(List.of(c1));
        when(cartaoClient.buscarCartao(any())).thenReturn(cartao);
        
        PaypalApiException ex = new PaypalApiException(
            500,
            "ERROR",
             Collections.emptyList());

        doThrow(ex).when(paypalClient).autorizarTransacao(any(), any());

        assertThrows(PaypalApiException.class, () -> service.processarFilaCobranca());
        
        verify(repository, never()).save(any(Cobranca.class)); 
    }

    @Test
    @DisplayName("Deve retornar DTO com sucesso (Branch ID não nulo e encontrado)")
    void testBuscarCobrancaSucesso() {
        Long id = 1L; 
        
        when(repository.findById(id)).thenReturn(Optional.of(cobranca));
        
        when(mapper.toDTO(any(Cobranca.class))).thenReturn(cobrancaDTO);

        CobrancaDTO result = service.buscarCobranca(id);

        assertNotNull(result);
        assertEquals(cobrancaDTO.getId(), result.getId());
        
        verify(repository).findById(id);
        verify(mapper).toDTO(cobranca);
    }

    @Test
    @DisplayName("Deve lançar ObjectNotFoundException se cobrança não existir")
    void testBuscarCobrancaNotFound() {
        Long id = 1L; 
        
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.buscarCobranca(id));
        verify(repository).findById(id);
        
        verifyNoInteractions(mapper); 
    }

    @Test
    @DisplayName("Deve retornar null se o ID for null ")
    void testBuscarCobrancaIdNull() {
        Long id = null;

        CobrancaDTO result = service.buscarCobranca(id);

        assertNull(result);
        
        verifyNoInteractions(repository); 
        verifyNoInteractions(mapper);
    }
}
