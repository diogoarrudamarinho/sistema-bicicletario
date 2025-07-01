package unirio.pm.external_service.services;

import java.util.List;

import unirio.pm.external_service.dto.CobrancaDTO;
import unirio.pm.external_service.dto.CobrancaRequestDTO;

public interface CobrancaService {
    
    CobrancaDTO criarCobranca(CobrancaRequestDTO cobranca);
    List<CobrancaDTO> processarFilaCobranca();

}
