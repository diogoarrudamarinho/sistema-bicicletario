package dev.unirio.externalservice.client.paypal;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

class PapalAuthClientTest {

    private MockWebServer mockWebServer; 
    
    private PaypalAuthClient authClient;
    private PaypalAuthClient.Response responseClass; 

    private final String clientId = "test-client-id";
    private final String clientSecret = "test-client-secret";
    private final String expectedToken = "TEST_ACCESS_TOKEN_12345";
    
    private final String mockResponseJson = String.format("""
        {
          "scope": "openid",
          "access_token": "%s",
          "token_type": "Bearer",
          "app_id": "APP-0000000000000000",
          "expires_in": 32400
        }
        """, expectedToken);


    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        String mockBaseUrl = mockWebServer.url("/").toString();
        WebClient.Builder webClientBuilder = WebClient.builder();

        authClient = new PaypalAuthClient(webClientBuilder, clientId, clientSecret, mockBaseUrl);
        
        responseClass = new PaypalAuthClient.Response();
    }

    @AfterEach
    @SuppressWarnings("unused")
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    
    @Test
    @DisplayName("Deve retornar o token de acesso após chamada")
    void getAccessTokenSucesso() {
        
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(mockResponseJson));

        String token = authClient.getAccessToken();
        
        assertNotNull(token);
        assertEquals(expectedToken, token);
    }

    @Test
    @DisplayName("Deve retornar Exception")
    void getAccessTokenError() {
        
       mockWebServer.enqueue(new MockResponse()
            .setResponseCode(401)
            .setHeader("Content-Type", "application/json"));

        WebClientResponseException exception = assertThrows(WebClientResponseException.Unauthorized.class, () -> {
            authClient.getAccessToken();
        });
        
        assertNotNull(exception);
    }

    @Test
    @DisplayName("Deve funcionar corretamente os getters e setters")
    void responseClass_ShouldHandleDataCorrectly() {
        
        String newToken = "VALID_TOKEN";
        
        responseClass.setAcessToken(newToken);
        
        assertEquals(newToken, responseClass.getAccessToken());
    }
}
