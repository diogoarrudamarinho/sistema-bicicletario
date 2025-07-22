package unirio.pm.external_service.client.cartao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import reactor.core.publisher.Mono;
import unirio.pm.external_service.dto.CartaoDTO;

class CartaoClientTest {

    @Mock
    private WebClient.Builder builder;
    @Mock
    private WebClient webClient;
    @Mock
    private RequestHeadersUriSpec<?> requestHeadersUriSpec;
    @Mock
    private RequestHeadersSpec<?> requestHeadersSpec;
    @Mock
    private ResponseSpec responseSpec;

    private CartaoClient cartaoClient;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(builder.baseUrl(anyString())).thenReturn(builder);
        when(builder.build()).thenReturn(webClient);

        when(((RequestHeadersUriSpec) requestHeadersUriSpec).uri(anyString(), any(Object[].class)))
        .thenReturn((RequestHeadersSpec) requestHeadersSpec);       

        when((webClient).get()).thenReturn((RequestHeadersUriSpec) requestHeadersUriSpec);

        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        CartaoDTO cartaoDTO = new CartaoDTO("Titular", "4002356147465716", "2010-06", "584");
        when(responseSpec.bodyToMono(CartaoDTO.class)).thenReturn(Mono.just(cartaoDTO));

        cartaoClient = new CartaoClient(builder);
    }

    @Test
    void testBuscarCartao() {
        CartaoDTO dto = cartaoClient.buscarCartao(1L);

        assertEquals("Titular", dto.getTitular());
        assertEquals("4002356147465716", dto.getNumero());
        assertEquals("2010-06", dto.getValidade());
        assertEquals("584", dto.getCvv());

        verify(webClient).get();
        verify(requestHeadersUriSpec).uri("/cartaoDeCredito/{id}", 1L);
        verify(requestHeadersSpec).retrieve();
        verify(responseSpec).bodyToMono(CartaoDTO.class);
    }
}