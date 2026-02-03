package dev.unirio.rentalservice.dto;

import java.math.BigDecimal;

public record CobrancaRequestDTO(
    Long ciclista,
    BigDecimal valor
) {}
