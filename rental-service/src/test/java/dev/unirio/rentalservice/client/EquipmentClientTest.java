package dev.unirio.rentalservice.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import dev.unirio.rentalservice.dto.BicicletaDTO;
import dev.unirio.rentalservice.dto.TrancaDTO;
import dev.unirio.rentalservice.enumeration.BicicletaStatus;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@ExtendWith(MockitoExtension.class)
public class EquipmentClientTest {
    private static MockWebServer mockWebServer;
    private EquipmentClient equipmentClient;

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
        equipmentClient = new EquipmentClient(WebClient.builder(), baseUrl);
    }

    // --- TESTES DA FUNÇÃO: getBicicleta ---

    @Test
    void getBicicleta() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"id\": 1, \"modelo\": \"Caloi\"}")
                .addHeader("Content-Type", "application/json")
                .setResponseCode(200));

        BicicletaDTO response = equipmentClient.getBicicleta(1L);

        assertNotNull(response);
        assertEquals(1L, response.id());
    }

    // --- TESTES DA FUNÇÃO: getBicicletaByTranca ---

    @Test
    void getBicicletaByTranca() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"id\": 10, \"modelo\": \"Monark\"}")
                .addHeader("Content-Type", "application/json")
                .setResponseCode(200));

        BicicletaDTO response = equipmentClient.getBicicletaByTranca(5L);

        assertNotNull(response);
        assertEquals(10L, response.id());
    }

    // --- TESTES DA FUNÇÃO: postAlterarStatusBicicleta ---

    @Test
    void postAlterarStatusBicicleta() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"id\": 1, \"status\": \"DISPONIVEL\"}")
                .addHeader("Content-Type", "application/json")
                .setResponseCode(200));

        BicicletaDTO response = equipmentClient.postAlterarStatusBicicleta(1L, BicicletaStatus.DISPONIVEL);

        assertNotNull(response);
    }

    // --- TESTES DA FUNÇÃO: postDestrancar ---

    @Test
    void postDestrancar() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"id\": 5, \"status\": \"LIVRE\"}")
                .addHeader("Content-Type", "application/json")
                .setResponseCode(200));

        TrancaDTO response = equipmentClient.postDestrancar(5L, 1L);

        assertNotNull(response);
        assertEquals(5L, response.getId());
    }
}
