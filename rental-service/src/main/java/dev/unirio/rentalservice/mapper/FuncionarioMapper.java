package dev.unirio.rentalservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import dev.unirio.rentalservice.dto.FuncionarioDTO;
import dev.unirio.rentalservice.entity.Funcionario;

@Mapper(componentModel = "spring")
public interface FuncionarioMapper {
    
    FuncionarioDTO toDto(Funcionario entity);
    Funcionario toEntity(FuncionarioDTO dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(FuncionarioDTO dto, @MappingTarget Funcionario entity);
}
