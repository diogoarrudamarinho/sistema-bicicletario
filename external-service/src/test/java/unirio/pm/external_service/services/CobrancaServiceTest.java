package unirio.pm.external_service.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import unirio.pm.external_service.client.cartao.CartaoClient;
import unirio.pm.external_service.client.paypal.PaypalClient;
import unirio.pm.external_service.dto.CartaoDTO;
import unirio.pm.external_service.dto.CobrancaDTO;
import unirio.pm.external_service.dto.CobrancaRequestDTO;
import unirio.pm.external_service.entity.Cobranca;
import unirio.pm.external_service.entity.FilaCobranca;
import unirio.pm.external_service.enumerations.StatusCobranca;
import unirio.pm.external_service.exception.ObjectNotFoundException;
import unirio.pm.external_service.exception.cobranca.PaypalApiException;
import unirio.pm.external_service.exception.cobranca.PaypalApiException.PaypalErrorDetail;
import unirio.pm.external_service.repository.CobrancaRepository;
import unirio.pm.external_service.repository.FilaCobrancaRepository;
import unirio.pm.external_service.services.implamentation.CobrancaServiceImplementation;

@ExtendWith(MockitoExtension.class)
public class CobrancaServiceTest {
    @InjectMocks
    private CobrancaServiceImplementation service;

    @Mock
    private CobrancaRepository cobrancaRepository;

    @Mock
    private FilaCobrancaRepository filaRepository;

    @Mock
    private PaypalClient paypalClient;

    @Mock
    private CartaoClient cartaoClient;

    private CartaoDTO cartao;
    private CobrancaRequestDTO request;
    private Cobranca cobranca;
    private FilaCobranca fila;

    @BeforeEach
    @SuppressWarnings("unused") //falso positivo
    void setup() {
        cartao = new CartaoDTO("Titular",
                                "4111111111111111",
                                "2030-12",
                                "123");

        request = new CobrancaRequestDTO();
        request.setCiclista(1L);
        request.setValor(new BigDecimal("100.00"));

        fila = new FilaCobranca(new BigDecimal("100.00"), 1L);

        cobranca = new Cobranca();
        cobranca.setId(10L);
        cobranca.setValor(new BigDecimal("100.00"));
        cobranca.setCiclista(1L);
        cobranca.setStatus(StatusCobranca.PAGA);
        cobranca.setHoraFinalizacao(LocalDateTime.now());
    }

    @Test
    @DisplayName("Deve criar cobrança com sucesso quando paypal autoriza")
    void testCriarCobrancaSucesso() {
        when(cartaoClient.buscarCartao(request.getCiclista())).thenReturn(cartao);
        doNothing().when(paypalClient).autorizarTransacao(cartao, request.getValor());

        when(cobrancaRepository.save(any(Cobranca.class))).thenReturn(cobranca);

        CobrancaDTO dto = service.criarCobranca(request);

        assertNotNull(dto);
        assertEquals(cobranca.getId(), dto.getId());
        assertEquals(StatusCobranca.PAGA, dto.getStatus());
        verify(filaRepository, never()).save(any(FilaCobranca.class)); 
        verify(paypalClient).autorizarTransacao(cartao, request.getValor());
    }

    @Test
    @DisplayName("Deve salvar cobrança na fila e lançar exceção quando erro de cartão")
    void testCriarCobrancaErroCartao() {
        when(cartaoClient.buscarCartao(request.getCiclista())).thenReturn(cartao);

        PaypalErrorDetail detail = mock(PaypalErrorDetail.class);
        when(detail.getIssue()).thenReturn("CARD_DECLINED");

        PaypalApiException exception = new PaypalApiException(
            422, "UNPROCESSABLE_ENTITY", Collections.singletonList(detail)
        );

        doThrow(exception).when(paypalClient).autorizarTransacao(cartao, request.getValor());

        PaypalApiException thrown = assertThrows(
            PaypalApiException.class, () -> {
            service.criarCobranca(request);
        });

        assertEquals("UNPROCESSABLE_ENTITY", thrown.getName());
        verify(filaRepository).save(any(FilaCobranca.class)); 
        verify(cobrancaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção sem salvar na fila quando erro não relacionado ao cartão")
    void testCriarCobrancaErro() {
        when(cartaoClient.buscarCartao(request.getCiclista())).thenReturn(cartao);

        PaypalErrorDetail detail = mock(PaypalErrorDetail.class);
        when(detail.getIssue()).thenReturn("ERROR");

        PaypalApiException exception = new PaypalApiException(
            500, "INTERNAL_SERVER_ERROR", Collections.singletonList(detail)
        );

        doThrow(exception).when(paypalClient).autorizarTransacao(cartao, request.getValor());

        PaypalApiException thrown = assertThrows(PaypalApiException.class, () -> {
            service.criarCobranca(request);
        });

        assertEquals("INTERNAL_SERVER_ERROR", thrown.getName());
        verify(filaRepository, never()).save(any());
        verify(cobrancaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve ignorar quando cartão não é encontrado")
    void testCartaoNull() {
        when(filaRepository.findAll()).thenReturn(List.of(fila));
        when(cartaoClient.buscarCartaoCerto(1L)).thenReturn(null);

        List<CobrancaDTO> resultado = service.processarFilaCobranca();

        assertTrue(resultado.isEmpty()); // Agora verifica retorno

        verify(paypalClient, never()).autorizarTransacao(any(), any());
        verify(cobrancaRepository, never()).save(any());
        verify(filaRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Deve processar e remover da fila quando transação for autorizada")
    void testProcessamentoComSucesso() {
    
        when(filaRepository.findAll()).thenReturn(List.of(fila));
        when(cartaoClient.buscarCartaoCerto(1L)).thenReturn(cartao);
        doNothing().when(paypalClient).autorizarTransacao(cartao, fila.getValor());

        when(cobrancaRepository.save(any())).thenAnswer(invocation -> {
            Cobranca entity = invocation.getArgument(0);
            entity.setId(123L); 
            return entity;
        });

        List<CobrancaDTO> resultado = service.processarFilaCobranca();

        assertEquals(1, resultado.size());
        assertEquals(fila.getValor(), resultado.get(0).getValor());
        assertEquals(fila.getCiclista(), resultado.get(0).getCiclista());
        assertEquals(StatusCobranca.PAGA, resultado.get(0).getStatus());

        verify(filaRepository).delete(fila);
        verify(cobrancaRepository).save(any());
    }

    @Test
    @DisplayName("Deve ignorar quando falha com erro de cartão")
    void testErroCartao() {

        PaypalErrorDetail detail = mock(PaypalErrorDetail.class);
        when(detail.getIssue()).thenReturn("CARD_DECLINED");

        PaypalApiException exception = new PaypalApiException(422, "Erro", List.of(detail));

        when(filaRepository.findAll()).thenReturn(List.of(fila));
        when(cartaoClient.buscarCartaoCerto(1L)).thenReturn(cartao);
        doThrow(exception).when(paypalClient).autorizarTransacao(cartao, fila.getValor());

        List<CobrancaDTO> resultado = assertDoesNotThrow(() -> service.processarFilaCobranca());

        assertTrue(resultado.isEmpty());

        verify(cobrancaRepository, never()).save(any());
        verify(filaRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Deve relançar exceção quando erro não é de cartão")
    void testFalhaErroGenerico() {
        
        PaypalErrorDetail detail = mock(PaypalErrorDetail.class);
        when(detail.getIssue()).thenReturn("INTERNAL_SERVER_ERROR");

        PaypalApiException exception = new PaypalApiException(500, "Erro", List.of(detail));

        when(filaRepository.findAll()).thenReturn(List.of(fila));
        when(cartaoClient.buscarCartaoCerto(1L)).thenReturn(cartao);
        doThrow(exception).when(paypalClient).autorizarTransacao(cartao, fila.getValor());

        PaypalApiException thrown = assertThrows(PaypalApiException.class, () -> {
            service.processarFilaCobranca();
        });

        assertEquals("Erro", thrown.getName());

        verify(cobrancaRepository, never()).save(any());
        verify(filaRepository, never()).delete(any());
    }

    @Test
    void testBuscarCobrancaSucesso() {
        Long id = 1L;
        Cobranca cobrancaLocal = new Cobranca();
        cobrancaLocal.setId(id);
        cobrancaLocal.setValor(new BigDecimal("100.00"));

        when(cobrancaRepository.findById(id)).thenReturn(Optional.of(cobrancaLocal));

        CobrancaDTO dto = service.buscarCobranca(id);

        assertNotNull(dto);
        assertEquals(id, dto.getId());
        assertEquals(cobrancaLocal.getValor(), dto.getValor());

        verify(cobrancaRepository).findById(id);
    }

    @Test
    void testBuscarCobrancaErro() {
        Long id = 1L;
        when(cobrancaRepository.findById(id)).thenReturn(Optional.empty());

        ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> {
            service.buscarCobranca(id);
        });

        assertEquals("Not Found", ex.getMessage());
        verify(cobrancaRepository).findById(id);
    }

}
