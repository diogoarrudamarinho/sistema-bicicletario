package dev.unirio.rentalservice.client;

import dev.unirio.rentalservice.dto.CobrancaRequestDTO;
import dev.unirio.rentalservice.dto.CobrancaResponseDTO;
import dev.unirio.rentalservice.dto.CartaoDTO;
import dev.unirio.rentalservice.dto.EmailDTO;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExternalClientTest {

    private static MockWebServer mockWebServer;
    private ExternalClient externalClient;

    @BeforeAll
    static void setUpAll() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDownAll() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void setUp() {
        String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        externalClient = new ExternalClient(WebClient.builder(), baseUrl);
    }

    // --- TESTES DA FUNÇÃO: postRealizaCobranca ---

    @Test
    void postRealizaCobranca() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"id\": 500, \"status\": \"PAGA\"}")
                .addHeader("Content-Type", "application/json")
                .setResponseCode(200));

        CobrancaResponseDTO response = externalClient.postRealizaCobranca(new CobrancaRequestDTO(1L, new BigDecimal("5.00")));

        assertNotNull(response);
        assertEquals(500L, response.id());
    }

    @Test
    void postRealizaCobrancaFalhaAoRealizarCobranca() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(400));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> 
            externalClient.postRealizaCobranca(new CobrancaRequestDTO(1L, BigDecimal.TEN)));
        
        assertEquals("Falha ao realizar cobranca", ex.getMessage());
    }

    // --- TESTES DA FUNÇÃO: postEstornarCobranca ---

    @Test
    void postEstornarCobranca() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));

        assertDoesNotThrow(() -> externalClient.postEstornarCobranca(500L));
    }

    @Test
    void postEstornarCobrancaFalhaAoEstornar() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> 
            externalClient.postEstornarCobranca(500L));
        
        assertEquals("Falha ao estornar", ex.getMessage());
    }

    // --- TESTES DA FUNÇÃO: validarCartao ---

    @Test
    void validarCartao() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));
        
        CartaoDTO dto = new CartaoDTO(1L, "Nome", "123", null, "123");
        assertDoesNotThrow(() -> externalClient.validarCartao(dto));
    }

    @Test
    void validarCartaoFalhaNaValidacao() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(401));

        CartaoDTO dto = new CartaoDTO(1L, "Nome", "123", null, "123");
        RuntimeException ex = assertThrows(RuntimeException.class, () -> 
            externalClient.validarCartao(dto));
        
        assertEquals("Falha na validação", ex.getMessage());
    }

    // --- TESTES DA FUNÇÃO: postEmail ---

    @Test
    void postEmail() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"destinatario\": \"test@test.com\", \"assunto\": \"Oi\", \"mensagem\": \"Corpo\"}")
                .addHeader("Content-Type", "application/json")
                .setResponseCode(200));

        EmailDTO response = externalClient.postEmail(new EmailDTO("test@test.com", "Oi", "Corpo"));

        assertNotNull(response);
    }
}