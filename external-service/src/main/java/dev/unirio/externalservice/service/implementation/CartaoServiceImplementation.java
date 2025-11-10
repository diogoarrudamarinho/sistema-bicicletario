package dev.unirio.externalservice.service.implementation;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import dev.unirio.externalservice.client.paypal.PaypalClient;
import dev.unirio.externalservice.dto.CartaoDTO;
import dev.unirio.externalservice.service.CartaoService;

@Service
public class CartaoServiceImplementation implements CartaoService{
    
    private final PaypalClient client;
    
    public CartaoServiceImplementation(PaypalClient client) {
        this.client = client;
    }

    @Override
    public void validarCartao(CartaoDTO cartao){
        client.autorizarTransacao(cartao, new BigDecimal("0.01"));
    }

}