package dev.unirio.rentalservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import dev.unirio.rentalservice.enumeration.StatusCobranca;

public record CobrancaResponseDTO(
    Long id,
    BigDecimal valor,
    Long ciclista,
    StatusCobranca status,
    LocalDateTime horaSolicitacao,
    LocalDateTime horaFinalizacao
) {
    
}
