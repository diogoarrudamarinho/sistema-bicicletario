package dev.unirio.externalservice.service;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.unirio.externalservice.client.paypal.PaypalClient;
import dev.unirio.externalservice.dto.CartaoDTO;
import dev.unirio.externalservice.service.implementation.CartaoServiceImplementation;

@ExtendWith(MockitoExtension.class)
class CartaoServiceTest {
    
    @Mock
    private PaypalClient client;

    @InjectMocks
    private CartaoServiceImplementation service;

    @Test
    @DisplayName("Deve chamar o client para validar o cartão")
    void testValidarCartao() {

        CartaoDTO cartao = new CartaoDTO("Titular",
                                        "4111111111111111",
                                        "2030-01",
                                        "123");

        BigDecimal valor = new BigDecimal("0.01");

        doNothing().when(client).autorizarTransacao(cartao, valor);

        service.validarCartao(cartao);
        verify(client).autorizarTransacao(cartao, valor);
    }
}
