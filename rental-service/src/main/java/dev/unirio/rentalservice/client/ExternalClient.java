package dev.unirio.rentalservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import dev.unirio.rentalservice.dto.CartaoDTO;
import dev.unirio.rentalservice.dto.EmailDTO;
import reactor.core.publisher.Mono;

@Component
public class ExternalClient {
    
    private final WebClient client;

    public ExternalClient(WebClient.Builder builder, @Value("${services.url.externalservice}") String baseUrl){
        client = builder.baseUrl(baseUrl).build();
    }

    public void validarCartao(CartaoDTO dto){
        client.post()
            .uri("/validaCartaoDeCredito")
            .bodyValue(dto)
            .retrieve()
            .onStatus(HttpStatusCode::isError, response -> Mono.error(new RuntimeException("Falha na validação")))
            .toBodilessEntity()
            .block();
    }

    public EmailDTO postEmail(EmailDTO dto) {
        return client.post()
            .uri("/enviarEmail")
            .bodyValue(dto)
            .retrieve()
            .bodyToMono(EmailDTO.class)
            .block(); 
    }
}
