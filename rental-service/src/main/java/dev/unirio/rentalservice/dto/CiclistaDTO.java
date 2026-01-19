package dev.unirio.rentalservice.dto;

import java.time.LocalDate;

import dev.unirio.rentalservice.entity.value.Passaporte;
import dev.unirio.rentalservice.enumeration.CiclistaStatus;
import dev.unirio.rentalservice.enumeration.Nacionalidade;

public record CiclistaDTO(
    Long id,
    String nome,
    String email,
    String cpf,
    Nacionalidade nacionalidade,
    LocalDate nascimento,
    Passaporte passaporte,
    CiclistaStatus status,
    Long urlFotoDocumento
) {}
