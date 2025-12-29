package dev.unirio.equipmentservice.service;

import java.util.List;

import dev.unirio.equipmentservice.dto.BicicletaDTO;
import dev.unirio.equipmentservice.dto.TrancaDTO;
import dev.unirio.equipmentservice.dto.TrancaIntegracaoDTO;
import dev.unirio.equipmentservice.dto.TrancaRequestDTO;
import dev.unirio.equipmentservice.enumeration.TrancaStatus;

public interface TrancaService {
    // Get
    List<TrancaDTO> buscarTrancas();
    TrancaDTO buscarTranca(Long id);
    BicicletaDTO buscarBicicleta(Long id);

    // Post
    TrancaDTO cadastrarTranca(TrancaRequestDTO novaTranca);
    TrancaDTO trancar(Long id, Long bicicletaId);
    TrancaDTO destrancar(Long id, Long bicicletaId);
    TrancaDTO alterarStatus(Long id, TrancaStatus status);
    Void integrarRede(TrancaIntegracaoDTO request);
    Void retirarRede(TrancaIntegracaoDTO request);
    
    // Put
    TrancaDTO atualizarTranca(Long id, TrancaRequestDTO novaTranca);

    // Del
    Void deletar(Long id);
}
