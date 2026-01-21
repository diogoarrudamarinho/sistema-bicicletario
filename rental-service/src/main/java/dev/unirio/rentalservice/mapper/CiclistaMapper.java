package dev.unirio.rentalservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import dev.unirio.rentalservice.dto.CiclistaDTO;
import dev.unirio.rentalservice.dto.CiclistaRequestDTO;
import dev.unirio.rentalservice.entity.Ciclista;

@Mapper(componentModel = "spring", uses = {CartaoMapper.class})
public interface CiclistaMapper {
    CiclistaDTO toDto(Ciclista entity);

    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "cartao", ignore = true)
    Ciclista toEntity(CiclistaDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    Ciclista toEntityFromRequest(CiclistaRequestDTO dto);
}
