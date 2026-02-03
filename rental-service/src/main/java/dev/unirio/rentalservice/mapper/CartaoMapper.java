package dev.unirio.rentalservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import dev.unirio.rentalservice.dto.CartaoDTO;
import dev.unirio.rentalservice.entity.Cartao;

@Mapper(componentModel = "spring")
public interface CartaoMapper {
    CartaoDTO toDto(Cartao entity);

    @Mapping(target = "ciclista", ignore = true)
    Cartao toEntity(CartaoDTO dto);

    @Mapping(target = "ciclista", ignore = true)
    void updateEntityFromDto(CartaoDTO dto, @MappingTarget Cartao entity);
}
