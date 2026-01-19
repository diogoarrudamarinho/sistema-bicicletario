package dev.unirio.rentalservice.dto;

import java.time.LocalDate;

public record CartaoDTO(
    Long id,
    String nomeTitular,
    String numero, 
    LocalDate validade,
    String cvv
) {}
