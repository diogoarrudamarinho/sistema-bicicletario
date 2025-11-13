package dev.unirio.externalservice.client.paypal.cartao;

import org.springframework.stereotype.Component;

import dev.unirio.externalservice.dto.CartaoDTO;

@Component
public class CartaoClient {
    
    public CartaoClient(){
        // Construtor vazio
    }

    public CartaoDTO buscarCartao(Long id) {      
        return new CartaoDTO("Diogo", "4032039366042275", "2027-08", "798");
    }
    
    public CartaoDTO busCartaoErro(){
        return new CartaoDTO("Diogo", "4032039366042275", "2023-08", "798");
    }
}
