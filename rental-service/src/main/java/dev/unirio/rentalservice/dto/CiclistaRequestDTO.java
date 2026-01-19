package dev.unirio.rentalservice.dto;

import java.time.LocalDate;
import java.util.List;

import dev.unirio.rentalservice.entity.value.Passaporte;
import dev.unirio.rentalservice.enumeration.Nacionalidade;

public record CiclistaRequestDTO(
    String nome,
    String email,
    String cpf,
    String senha,
    Nacionalidade nacionalidade,
    LocalDate nascimento,
    Passaporte passaporte,
    Long urlFotoDocumento,
    List<CartaoDTO> cartoes
) {}
