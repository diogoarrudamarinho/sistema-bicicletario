package dev.unirio.equipmentservice.service;

import dev.unirio.equipmentservice.dto.BicicletaDTO;
import dev.unirio.equipmentservice.dto.BicicletaRequestDTO;
import dev.unirio.equipmentservice.entity.Bicicleta;
import dev.unirio.equipmentservice.enumeration.BicicletaStatus;

public interface BicicletaService {
    // Get
    BicicletaDTO buscarBicicleta(Long id);

    // Post
    BicicletaDTO criarBicicleta(BicicletaRequestDTO novaBicicleta);
    BicicletaDTO alterarStatus(Long id, BicicletaStatus status);
    
    // Put
    BicicletaDTO atualizarBicicleta(Long id, BicicletaRequestDTO novaBicicleta);
    
    // Del
    void deletarBicicleta(Long id);

    // Métodos não mapeados
    Bicicleta buscarEntidade(Long id);

}
