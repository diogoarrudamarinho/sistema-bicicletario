package dev.unirio.equipmentservice.service;

import dev.unirio.equipmentservice.dto.BicicletaDTO;
import dev.unirio.equipmentservice.dto.BicicletaRequestDTO;

public interface BicicletaService {
    BicicletaDTO buscarBicicleta(Long id);
    BicicletaDTO criarBicicleta(BicicletaRequestDTO novaBicicleta);
    BicicletaDTO atualizarBicicleta(Long id, BicicletaRequestDTO novaBicicleta);
    void deletarBicicleta(Long id);
}
