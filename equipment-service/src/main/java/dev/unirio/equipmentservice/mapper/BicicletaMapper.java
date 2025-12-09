package dev.unirio.equipmentservice.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.unirio.equipmentservice.dto.BicicletaDTO;
import dev.unirio.equipmentservice.dto.BicicletaRequestDTO;
import dev.unirio.equipmentservice.entity.Bicicleta;

@Component
public class BicicletaMapper {

    public BicicletaDTO toDto(Bicicleta bicicleta) {
        
        if (bicicleta == null) {
            return null;
        }

        BicicletaDTO dto = new BicicletaDTO();
        dto.setId(bicicleta.getId());
        dto.setMarca(bicicleta.getMarca());
        dto.setModelo(bicicleta.getModelo());
        dto.setAno(bicicleta.getAno());
        dto.setNumero(bicicleta.getNumero());
        dto.setStatus(bicicleta.getStatus());
        
        return dto;
    }

    public List<BicicletaDTO> toDtoList(List<Bicicleta> bicicletas) {
       
        if (bicicletas == null) {
            return List.of();
        }

        return bicicletas.stream()
                .map(this::toDto)
                .toList();
    }

    public Bicicleta toEntity(BicicletaRequestDTO requestDTO) {
       
        if (requestDTO == null) {
            return null;
        }

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setMarca(requestDTO.getMarca());
        bicicleta.setModelo(requestDTO.getModelo());
        bicicleta.setAno(requestDTO.getAno());
        bicicleta.setNumero(requestDTO.getNumero());
        bicicleta.setStatus(requestDTO.getStatus());

        return bicicleta;
    }

    public void updateEntityFromDto(BicicletaRequestDTO requestDTO, Bicicleta bicicleta) {
        
        if (requestDTO == null || bicicleta == null) {
            return;
        }

        bicicleta.setMarca(requestDTO.getMarca());
        bicicleta.setModelo(requestDTO.getModelo());
        bicicleta.setAno(requestDTO.getAno());
        bicicleta.setNumero(requestDTO.getNumero());
        bicicleta.setStatus(requestDTO.getStatus());
    }
}