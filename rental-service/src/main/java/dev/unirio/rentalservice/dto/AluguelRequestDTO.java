package dev.unirio.rentalservice.dto;

import jakarta.validation.constraints.NotNull;

public record AluguelRequestDTO(
    @NotNull(message = "ciclista é obrigatório")
    Long ciclista,
    @NotNull(message = "tranca é obrigatório")
    Long tranca
) {}
