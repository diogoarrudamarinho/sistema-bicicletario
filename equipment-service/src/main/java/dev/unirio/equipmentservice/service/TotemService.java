package dev.unirio.equipmentservice.service;

import java.util.List;

import dev.unirio.equipmentservice.dto.BicicletaDTO;
import dev.unirio.equipmentservice.dto.TotemDTO;
import dev.unirio.equipmentservice.dto.TotemRequestDTO;
import dev.unirio.equipmentservice.dto.TrancaDTO;

public interface TotemService {
    // Get
    TotemDTO buscarTotem(Long id);
    List<TotemDTO> buscarTotens();
    List<TrancaDTO> buscarTrancas(Long id);
    List<BicicletaDTO> buscarBicicletas(Long id);
    
    // Post
    TotemDTO criarTotem(TotemRequestDTO novoTotem);

    // Put
    TotemDTO atualizarTotem(Long id, TotemRequestDTO totem);

    // Del
    Void deletarTotem(Long id);

}
