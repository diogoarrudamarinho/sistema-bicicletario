package br.com.vadebicicleta.scb.equipamento.mapper;

import br.com.vadebicicleta.scb.equipamento.dto.AlteraTotemDTO;
import br.com.vadebicicleta.scb.equipamento.dto.NovoTotemDTO;
import br.com.vadebicicleta.scb.equipamento.dto.TotemDTO;
import br.com.vadebicicleta.scb.equipamento.entity.Totem;
import org.springframework.stereotype.Component;

@Component
public class TotemMapper {

    public TotemDTO toDto(Totem totem) {
        if (totem == null) {
            return null;
        }
        TotemDTO dto = new TotemDTO();
        dto.setId(totem.getPublicId());
        dto.setLocalizacao(totem.getLocalizacao());
        dto.setDescricao(totem.getDescricao());
        return dto;
    }

    public Totem toEntity(NovoTotemDTO dto) {
        if (dto == null) {
            return null;
        }
        Totem totem = new Totem();
        totem.setLocalizacao(dto.getLocalizacao());
        totem.setDescricao(dto.getDescricao());
        return totem;
    }

    public void updateEntityFromDto(AlteraTotemDTO dto, Totem totem) {
        totem.setLocalizacao(dto.getLocalizacao());
        totem.setDescricao(dto.getDescricao());
    }
}