package dev.unirio.rentalservice.dto;

import java.time.LocalDateTime;

public record AluguelDTO(
    Long id,
    Long ciclista,
    Long bicicleta,
    Long trancaInicio,
    Long trancaFim,
    LocalDateTime horaInicio,
    LocalDateTime horaFim,
    Long cobranca
){}
