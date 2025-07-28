package br.com.vadebicicleta.scb.equipamento.mapper;

import org.springframework.stereotype.Component;

import br.com.vadebicicleta.scb.equipamento.dto.AlteraTrancaDTO;
import br.com.vadebicicleta.scb.equipamento.dto.NovaTrancaDTO;
import br.com.vadebicicleta.scb.equipamento.dto.TrancaDTO;
import br.com.vadebicicleta.scb.equipamento.entity.Tranca;

@Component
public class TrancaMapper {

    public TrancaDTO toDto(Tranca tranca) {
        if (tranca == null) {
            return null;
        }
        TrancaDTO dto = new TrancaDTO();
        dto.setId(tranca.getId());
        dto.setNumero(tranca.getNumero());
        dto.setModelo(tranca.getModelo());
        dto.setAnoDeFabricacao(tranca.getAnoDeFabricacao());
        dto.setStatus(tranca.getStatus().name());
        dto.setIdBicicleta(tranca.getBicicleta() != null ? tranca.getBicicleta().getId() : null);

        if (tranca.getTotem() != null) {
            dto.setIdTotem(tranca.getTotem().getId());
        }

        return dto;
    }

    public Tranca toEntity(NovaTrancaDTO dto) {
        if (dto == null) {
            return null;
        }
        Tranca tranca = new Tranca();
        tranca.setNumero(dto.getNumero());
        tranca.setModelo(dto.getModelo());
        tranca.setAnoDeFabricacao(dto.getAnoDeFabricacao());
        return tranca;
    }

    public void updateEntityFromDto(AlteraTrancaDTO dto, Tranca tranca) {
        tranca.setNumero(dto.getNumero());
        tranca.setModelo(dto.getModelo());
        tranca.setAnoDeFabricacao(dto.getAnoDeFabricacao());
    }
}