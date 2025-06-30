package unirio.pm.external_service.services.implamentation;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import unirio.pm.external_service.client.paypal.PaypalClient;
import unirio.pm.external_service.dto.CartaoDTO;
import unirio.pm.external_service.services.CartaoService;

@Service
public class CartaoServiceImplementation implements CartaoService{
    
    @Autowired
    private PaypalClient client;

    @Override
    public void validarCartao(CartaoDTO cartao){
        client.autorizarTransacao(cartao, new BigDecimal("0.01"));
    }

}
