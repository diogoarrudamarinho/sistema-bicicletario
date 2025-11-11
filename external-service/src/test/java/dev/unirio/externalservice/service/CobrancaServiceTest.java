package dev.unirio.externalservice.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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

}
