package br.com.vadebicicleta.scb.equipamento.mapper;

import org.springframework.stereotype.Component;

import br.com.vadebicicleta.scb.equipamento.dto.AlteraBicicletaDTO;
import br.com.vadebicicleta.scb.equipamento.dto.BicicletaDTO;
import br.com.vadebicicleta.scb.equipamento.dto.NovaBicicletaDTO;
import br.com.vadebicicleta.scb.equipamento.entity.Bicicleta;

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
        dto.setStatus(bicicleta.getStatus().name());
        return dto;
    }

    public Bicicleta toEntity(NovaBicicletaDTO dto) {
        if (dto == null) {
            return null;
        }
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setMarca(dto.getMarca());
        bicicleta.setModelo(dto.getModelo());
        bicicleta.setAno(dto.getAno());
        return bicicleta;
    }

    public void updateEntityFromDto(AlteraBicicletaDTO dto, Bicicleta bicicleta) {
        bicicleta.setMarca(dto.getMarca());
        bicicleta.setModelo(dto.getModelo());
        bicicleta.setAno(dto.getAno());
    }
}
