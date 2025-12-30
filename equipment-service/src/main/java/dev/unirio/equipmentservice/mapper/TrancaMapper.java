package dev.unirio.equipmentservice.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.unirio.equipmentservice.dto.TrancaDTO;
import dev.unirio.equipmentservice.dto.TrancaRequestDTO;
import dev.unirio.equipmentservice.entity.Tranca;

@Component
public class TrancaMapper {
    
    public TrancaDTO toDto(Tranca tranca) {
        if (tranca == null) return null;

        TrancaDTO dto = new TrancaDTO();
        dto.setId(tranca.getId());
        dto.setNumero(tranca.getNumero());
        dto.setLocalizacao(tranca.getLocalizacao());
        dto.setAnoDeFabricacao(tranca.getAnoDeFabricacao());
        dto.setModelo(tranca.getModelo());
        dto.setStatus(tranca.getStatus());

        // Extrai apenas o ID da bicicleta se ela existir
        if (tranca.getBicicleta() != null) {
            dto.setBicicleta(tranca.getBicicleta().getId());
        }

        return dto;
    }

    public List<TrancaDTO> toDtoList(List<Tranca> trancas) {
        if (trancas == null) return List.of();
        return trancas.stream().map(this::toDto).toList();
    }

    public Tranca toEntity(TrancaRequestDTO requestDTO) {
        if (requestDTO == null) return null;

        Tranca tranca = new Tranca();
        tranca.setNumero(requestDTO.getNumero());
        tranca.setLocalizacao(requestDTO.getLocalizacao());
        tranca.setAnoDeFabricacao(requestDTO.getAnoDeFabricacao());
        tranca.setModelo(requestDTO.getModelo());
        tranca.setStatus(requestDTO.getStatus());
        
        return tranca;
    }

    public void updateEntityFromDto(TrancaRequestDTO requestDTO, Tranca tranca) {
        if (requestDTO == null || tranca == null) return;

        tranca.setNumero(requestDTO.getNumero());
        tranca.setLocalizacao(requestDTO.getLocalizacao());
        tranca.setAnoDeFabricacao(requestDTO.getAnoDeFabricacao());
        tranca.setModelo(requestDTO.getModelo());
        tranca.setStatus(requestDTO.getStatus());
    }
}
