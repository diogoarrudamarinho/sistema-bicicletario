package dev.unirio.rentalservice.dto;

import dev.unirio.rentalservice.enumeration.BicicletaStatus;

public record BicicletaDTO(
    Long id,
    Integer numero,
    String marca,
    String modelo,
    String ano,
    BicicletaStatus status
) {}
