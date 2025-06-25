package unirio.pm.external_service.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import unirio.pm.external_service.dto.CartaoDTO;

@Component
public class CartaoClient {
    
    private final WebClient client;

    //Para quando acabarem a api
    public CartaoClient(WebClient.Builder builder) {
        this.client = builder.baseUrl(".../aluguel").build();
    }

    public CartaoDTO buscarCartao (Long id){
        /*return client.get()
                .uri("/cartaoDeCredito/{id}", id)
                .retrieve()
                .bodyToMono(CartaoDTO.class)
                .block();
        */

        // Cartao gerado pelo paypal pra testes
        return new CartaoDTO(
            "Titular",
            "4002356147465716",
            "2030-06",
            "584"
        );
    }
}
