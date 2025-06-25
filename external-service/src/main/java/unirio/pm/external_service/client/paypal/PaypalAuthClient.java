package unirio.pm.external_service.client.paypal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.cdimascio.dotenv.Dotenv;

@Component
public class PaypalAuthClient {

    private final WebClient webClient;
    private final String clientId;
    private final String clientSecret;

    public PaypalAuthClient(
        WebClient.Builder builder,
        Dotenv dotenv,
        @Value("${paypal.base-url}") String baseUrl 
    ) {
        this.clientId = dotenv.get("PAYPAL_CLIENT_ID");
        this.clientSecret = dotenv.get("PAYPAL_CLIENT_SECRET");
        this.webClient = builder
            .baseUrl(baseUrl)
            .build();
    }

    public String getAccessToken() {
        return webClient.post()
            .uri("/v1/oauth2/token")
            .headers(headers -> headers.setBasicAuth(clientId, clientSecret))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .body(BodyInserters.fromFormData("grant_type", "client_credentials"))
            .retrieve()
            .bodyToMono(Response.class)
            .map(Response::getAccessToken)
            .block();
    }

    private static class Response {
        @JsonProperty("access_token")
        private String accessToken;
        public String getAccessToken() { return accessToken; }
    }
}
