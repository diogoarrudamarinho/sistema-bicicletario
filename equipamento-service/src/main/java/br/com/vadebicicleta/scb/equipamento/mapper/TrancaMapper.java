package br.com.vadebicicleta.scb.equipamento.mapper;

import br.com.vadebicicleta.scb.equipamento.dto.AlteraTrancaDTO;
import br.com.vadebicicleta.scb.equipamento.dto.NovaTrancaDTO;
import br.com.vadebicicleta.scb.equipamento.dto.TrancaDTO;
import br.com.vadebicicleta.scb.equipamento.entity.Tranca;
import org.springframework.stereotype.Component;

@Component
public class TrancaMapper {

    public TrancaDTO toDto(Tranca tranca) {
        if (tranca == null) {
            return null;
        }
        TrancaDTO dto = new TrancaDTO();
        dto.setId(tranca.getPublicId());
        dto.setNumero(tranca.getNumero());
        dto.setModelo(tranca.getModelo());
        dto.setAnoDeFabricacao(tranca.getAnoDeFabricacao());
        dto.setStatus(tranca.getStatus().name());

        if (tranca.getTotem() != null) {
            dto.setIdTotem(tranca.getTotem().getPublicId());
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