package unirio.pm.external_service.client.paypal;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodyUriSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import reactor.core.publisher.Mono;
import unirio.pm.external_service.dto.CartaoDTO;
import unirio.pm.external_service.exception.cobranca.PaypalApiException;

class PaypalClientTest {
       @Mock
    private WebClient.Builder builder;

    @Mock
    private WebClient webClient;

    @Mock
    private PaypalAuthClient authClient;
    
    @Mock
    private RequestBodyUriSpec bodySpec;

    @Mock
    private RequestHeadersSpec<?> headersSpec;

    @Mock
    private ResponseSpec responseSpec;

    private PaypalClient paypalClient;

    @BeforeEach
    @SuppressWarnings("unused")
    void setup() {
       MockitoAnnotations.openMocks(this);

        when(builder.baseUrl(anyString())).thenReturn(builder);
        when(builder.build()).thenReturn(webClient);

        paypalClient = new PaypalClient(builder, authClient, "https://api.sandbox.paypal.com");

    }

    @Test
    void testAutorizarTransacao_ComSucesso() {
        when(authClient.getAccessToken()).thenReturn("valid_token");

        // Mocks para a criação do pedido
        when(webClient.post()).thenReturn(bodySpec);
        when(bodySpec.uri(eq("/v2/checkout/orders"))).thenReturn(bodySpec);
        when(bodySpec.header(anyString(), anyString())).thenReturn(bodySpec);
        when(bodySpec.bodyValue(any())).thenReturn((RequestHeadersSpec) headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(eq(PaypalClient.Response.class)))
            .thenReturn(Mono.just(new PaypalClient.Response() {{
                id = "order123";
            }}));

        // Mocks para a captura do pagamento
        WebClient.RequestBodyUriSpec captureSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestHeadersSpec captureHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec captureResponseSpec = mock(WebClient.ResponseSpec.class);

        // IMPORTANTE: mockando .post() novamente para a segunda chamada (capture)
        when(webClient.post()).thenReturn(bodySpec).thenReturn(captureSpec);

        when(captureSpec.uri(eq("/v2/checkout/orders/{orderId}/capture"), eq("order123")))
            .thenReturn(captureSpec);
        when(captureSpec.header(anyString(), anyString())).thenReturn(captureSpec);
        when(captureSpec.header(eq("Content-Type"), eq("application/json"))).thenReturn(captureSpec);
        when(captureSpec.retrieve()).thenReturn(captureResponseSpec);
        when(captureResponseSpec.onStatus(any(), any())).thenReturn(captureResponseSpec);
        when(captureResponseSpec.bodyToMono(eq(PaypalClient.Response.class)))
            .thenReturn(Mono.just(new PaypalClient.Response() {{
                id = "captura123";
            }}));

        // Execução
        CartaoDTO cartao = new CartaoDTO("1234567890123456", "12/26", "Nome Teste", "123");
        assertDoesNotThrow(() -> paypalClient.autorizarTransacao(cartao, BigDecimal.TEN));
    }

    @Test
    void testAutorizarTransacao_TokenNulo() {
        when(authClient.getAccessToken()).thenReturn(null);
        CartaoDTO cartao = new CartaoDTO("123456", "12/26", "Fulano", "123");
        PaypalApiException ex = assertThrows(PaypalApiException.class, () -> paypalClient.autorizarTransacao(cartao, BigDecimal.ONE));
        assertEquals("TOKEN_ERROR", ex.getMessage());
    }

    @Test
    void testAutorizarTransacao_ErroInterno() {
        when(authClient.getAccessToken()).thenReturn("valid_token");
        when(webClient.post()).thenThrow(new RuntimeException("erro desconhecido"));
        CartaoDTO cartao = new CartaoDTO("123456", "12/26", "Fulano", "123");

        PaypalApiException ex = assertThrows(PaypalApiException.class, () -> paypalClient.autorizarTransacao(cartao, BigDecimal.ONE));
        assertEquals("Erro interno", ex.getMessage());
    }

    @Test
    void testAutorizarTransacao_ErroPaypalAPI() {
        when(authClient.getAccessToken()).thenReturn("valid_token");

    when(webClient.post()).thenReturn(bodySpec);
    when(bodySpec.uri(anyString())).thenReturn(bodySpec);
    when(bodySpec.header(anyString(), anyString())).thenReturn(bodySpec);
    when(bodySpec.bodyValue(any())).thenReturn((RequestHeadersSpec) headersSpec);
    when(headersSpec.retrieve()).thenReturn(responseSpec);

    // Simula que o status é erro e dispara a exceção PaypalApiException
    when(responseSpec.onStatus(any(), any()))
        .thenAnswer(invocation -> {
            java.util.function.Predicate statusPredicate = invocation.getArgument(0);
            java.util.function.Function clientResponseFunction = invocation.getArgument(1);
            return Mono.error(new PaypalApiException(500, "INTERNAL_SERVER_ERROR", Collections.emptyList()));
        });

    CartaoDTO cartao = new CartaoDTO("1234567890123456", "12/26", "Nome Teste", "123");

    PaypalApiException ex = assertThrows(PaypalApiException.class,
        () -> paypalClient.autorizarTransacao(cartao, BigDecimal.TEN));

    assertEquals("INTERNAL_SERVER_ERROR", ex.getName());
    }
}
