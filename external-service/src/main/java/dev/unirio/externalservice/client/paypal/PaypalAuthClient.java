package dev.unirio.externalservice.client.paypal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class PaypalAuthClient {

    private final WebClient webClient;
    private final String clientId;
    private final String clientSecret;

    @SuppressWarnings("null")
    public PaypalAuthClient(
        WebClient.Builder builder,
        @Value("${paypal.client-id}") String clientId,
        @Value("${paypal.client-secret}") String clientSecret,
        @Value("${paypal.base-url}") String baseUrl 
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.webClient = builder
            .baseUrl(baseUrl)
            .build();
    }

    @SuppressWarnings("null")
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

    static class Response {
        @JsonProperty("access_token")
        private String accessToken;
        public String getAccessToken() { return accessToken; }
        public void setAcessToken(String token){accessToken = token;}
    }
}
