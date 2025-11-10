package dev.unirio.externalservice.client.paypal;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import dev.unirio.externalservice.config.paypal.PaypalProperties;
import dev.unirio.externalservice.dto.CartaoDTO;
import dev.unirio.externalservice.exception.cobranca.PaypalApiException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@ExtendWith(MockitoExtension.class)
class PaypalClientTest {

    private static MockWebServer mockWebServer;
    private PaypalClient paypalClient;
    private CartaoDTO mockCartao;
    
    @Mock
    private PaypalAuthClient authClient;

    private final String validToken = "validToken";
    private final String orderId = "order123";
    
    private final String sucessOrderJson = String.format("{\"id\": \"%s\"}", orderId);
    private final String errorBodyJson = """
        {
          "name": "INTERNAL_SERVER_ERROR",
          "details": [{"issue": "CARD_DECLINED", "description": "Card was declined."}]
        }
    """;

    @BeforeAll
    @SuppressWarnings("unused")
    static void beforeAll() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    @SuppressWarnings("unused")
    static void afterAll() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        PaypalProperties properties = new PaypalProperties();
        properties.setBaseUrl(mockWebServer.url("/").toString());
        WebClient.Builder builder = WebClient.builder();
        
        paypalClient = new PaypalClient(builder, authClient, properties);
        mockCartao = new CartaoDTO("1234567890123456", "202612", "Nome Teste", "123");
    }

    @Test
    @DisplayName("Deve capturar a transacao")
    void testAutorizarTransacaoSucesso() {
        when(authClient.getAccessToken()).thenReturn(validToken);

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(sucessOrderJson)); 
        
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(sucessOrderJson)); 
        
        assertDoesNotThrow(() -> paypalClient.autorizarTransacao(mockCartao, BigDecimal.TEN));
    }

    @Test
    @DisplayName("Deve lançar exception para token nulo")
    void testAutorizarTransacaoTokenNulo() {
        when(authClient.getAccessToken()).thenReturn(null);
        
        PaypalApiException ex = assertThrows(PaypalApiException.class, 
            () -> paypalClient.autorizarTransacao(mockCartao, BigDecimal.ONE));
            
        assertEquals("TOKEN_ERROR", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar PaypalApiException para erro 400 da API")
    void testAutorizarTransacaoErroPaypalAPI() {

        when(authClient.getAccessToken()).thenReturn(validToken);
        
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setHeader("Content-Type", "application/json")
                .setBody(errorBodyJson)); 
        
        PaypalApiException ex = assertThrows(PaypalApiException.class,
            () -> paypalClient.autorizarTransacao(mockCartao, BigDecimal.TEN));
        
        assertEquals("INTERNAL_SERVER_ERROR", ex.getName());
        assertEquals(400, ex.getStatusCode());
    }

    
    @Test
    @DisplayName("Deve encapsular RuntimeException como PaypalApiException 'Erro interno'")
    void testAutorizarTransacaoErroInterno() {
        when(authClient.getAccessToken()).thenReturn(validToken);
        
        mockWebServer.enqueue(new MockResponse().setResponseCode(500).setBody("Server Error"));
        
        PaypalApiException ex = assertThrows(PaypalApiException.class, 
            () -> paypalClient.autorizarTransacao(mockCartao, BigDecimal.ONE));

        assertEquals("Erro interno", ex.getMessage(), "O erro deve ser encapsulado como 'Erro interno'.");
    }
}
