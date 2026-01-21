package dev.unirio.externalservice.client.paypal.cartao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import dev.unirio.externalservice.dto.CartaoDTO;

@Component
public class CartaoClient {
    
    private final WebClient client;

    public CartaoClient(WebClient.Builder builder, @Value("${services.url.rentalservice}") String baseUrl){
        client = builder.baseUrl(baseUrl).build();
    }

    public CartaoDTO buscarCartao (Long id){
        return client.get()
                .uri("/cartaoDeCredito/{id}", id)
                .retrieve()
                .bodyToMono(CartaoDTO.class)
                .block();
    }
}
