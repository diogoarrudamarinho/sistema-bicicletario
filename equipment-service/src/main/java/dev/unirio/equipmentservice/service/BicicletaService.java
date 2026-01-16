package dev.unirio.equipmentservice.service;

import java.util.List;

import dev.unirio.equipmentservice.dto.BicicletaDTO;
import dev.unirio.equipmentservice.dto.BicicletaIntegracaoDTO;
import dev.unirio.equipmentservice.dto.BicicletaRequestDTO;
import dev.unirio.equipmentservice.enumeration.BicicletaStatus;

public interface BicicletaService {
    // Get
    List<BicicletaDTO> buscarBicicletas();
    BicicletaDTO buscarBicicleta(Long id);

    // Post
    BicicletaDTO criarBicicleta(BicicletaRequestDTO novaBicicleta);
    BicicletaDTO alterarStatus(Long id, BicicletaStatus status);
    void integrarRede(BicicletaIntegracaoDTO request);
    void retirarRede(BicicletaIntegracaoDTO request);
    
    // Put
    BicicletaDTO atualizarBicicleta(Long id, BicicletaRequestDTO novaBicicleta);
    
    // Del
    void deletarBicicleta(Long id);

}
