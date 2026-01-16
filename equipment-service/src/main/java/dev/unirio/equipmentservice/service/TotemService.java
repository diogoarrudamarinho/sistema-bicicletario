package dev.unirio.equipmentservice.service;

import java.util.List;

import dev.unirio.equipmentservice.dto.TotemDTO;
import dev.unirio.equipmentservice.dto.TotemRequestDTO;

public interface TotemService {
    // Get
    TotemDTO buscarTotem(Long id);
    List<TotemDTO> buscarTotens();
    
    // Post
    TotemDTO criarTotem(TotemRequestDTO novoTotem);

    // Put
    TotemDTO atualizarTotem(Long id, TotemRequestDTO totem);

    // Del
    void deletarTotem(Long id);
}
