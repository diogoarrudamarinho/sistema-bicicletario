package dev.unirio.equipmentservice.mapper;

import dev.unirio.equipmentservice.dto.TotemDTO;
import dev.unirio.equipmentservice.dto.TotemRequestDTO;
import dev.unirio.equipmentservice.entity.Totem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TotemMapper {

    public TotemDTO toDto(Totem totem) {
        if (totem == null) return null;

        TotemDTO dto = new TotemDTO();
        dto.setId(totem.getId());
        dto.setLocalizacao(totem.getLocalizacao());
        dto.setDescricao(totem.getDescricao());
        
        return dto;
    }

    public List<TotemDTO> toDtoList(List<Totem> totens) {
        if (totens == null) return List.of();
        return totens.stream().map(this::toDto).toList();
    }

    public Totem toEntity(TotemRequestDTO requestDTO) {
        if (requestDTO == null) return null;

        Totem totem = new Totem();
        totem.setLocalizacao(requestDTO.getLocalizacao());
        totem.setDescricao(requestDTO.getDescricao());
        
        return totem;
    }

    public void updateEntityFromDto(TotemRequestDTO requestDTO, Totem totem) {
        if (requestDTO == null || totem == null) return;

        totem.setLocalizacao(requestDTO.getLocalizacao());
        totem.setDescricao(requestDTO.getDescricao());
    }
}