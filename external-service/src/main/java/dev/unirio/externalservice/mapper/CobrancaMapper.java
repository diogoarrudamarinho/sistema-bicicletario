package dev.unirio.externalservice.mapper;

import org.springframework.stereotype.Component;

import dev.unirio.externalservice.dto.CobrancaDTO;
import dev.unirio.externalservice.entity.Cobranca;

@Component
public class CobrancaMapper {
     
    public CobrancaDTO toDTO(Cobranca entity) {
        if (entity == null) return null;

        return new CobrancaDTO(
            entity.getId(),
            entity.getValor(),
            entity.getCiclista(),
            entity.getStatus(),
            entity.getHoraSolicitacao(),
            entity.getHoraFinalizacao()
        );
    }

    public Cobranca toEntity(CobrancaDTO dto) {
        if (dto == null) return null;

        Cobranca entity = new Cobranca();
        entity.setId(dto.getId());
        entity.setValor(dto.getValor());
        entity.setCiclista(dto.getCiclista());
        entity.setStatus(dto.getStatus());
        entity.setHoraSolicitacao(dto.getHoraSolicitacao());
        entity.setHoraFinalizacao(dto.getHoraFinalizacao());

        return entity;
    }
}
