package unirio.pm.external_service.services;

import unirio.pm.external_service.dto.CartaoDTO;

public interface CartaoService {
    
    void validarCartao(CartaoDTO cartao);
}
