package dev.unirio.externalservice.service;

import java.util.List;

import dev.unirio.externalservice.dto.CobrancaDTO;
import dev.unirio.externalservice.dto.CobrancaRequestDTO;

public interface CobrancaService {
    
    CobrancaDTO criarCobranca(CobrancaRequestDTO cobranca);
    CobrancaDTO buscarCobranca(Long id);
    List<CobrancaDTO> processarFilaCobranca();

}
