package dev.unirio.externalservice.client.paypal.cartao;

import org.springframework.stereotype.Component;

import dev.unirio.externalservice.dto.CartaoDTO;

@Component
public class CartaoClient {
    
    public CartaoClient(){
        // Construtor vazio
    }

    public CartaoDTO buscaCartao(Long id) {      
        return new CartaoDTO("Diogo", "4032039366042275", "08/2027", "798");
    }  
}
