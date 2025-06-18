package unirio.pm.external_service.services;

import unirio.pm.external_service.dto.CartaoDTO;
import unirio.pm.external_service.dto.CobrancaDTO;
import unirio.pm.external_service.dto.CobrancaRequestDTO;

public interface CobrancaService {
    
    CobrancaDTO criarCobranca(CobrancaRequestDTO cobranca);
    boolean validarCartao (CartaoDTO cartao);
}
