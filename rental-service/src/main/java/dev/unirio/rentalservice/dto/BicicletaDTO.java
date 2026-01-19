package dev.unirio.rentalservice.dto;

public record BicicletaDTO(
    Long id,
    Integer numero,
    String marca,
    String modelo,
    String ano,
    String status
) {}
