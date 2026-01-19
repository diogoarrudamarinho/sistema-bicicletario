package dev.unirio.rentalservice.mapper;

import org.mapstruct.Mapper;

import dev.unirio.rentalservice.dto.AluguelDTO;
import dev.unirio.rentalservice.entity.Aluguel;

@Mapper(componentModel = "spring")
public interface AluguelMapper {
    AluguelDTO toDto(Aluguel entity);
    Aluguel toEntity(AluguelDTO dto);
}
