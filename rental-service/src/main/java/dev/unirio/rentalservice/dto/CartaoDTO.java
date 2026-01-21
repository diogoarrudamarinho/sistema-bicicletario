package dev.unirio.rentalservice.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public record CartaoDTO(
    Long id,
    @NotBlank(message = "titular é obrigatórios")
    String nomeTitular,
    @NotBlank(message = "numero é obrigatórios")
    String numero, 
    @NotBlank(message = "validade é obrigatórios")
    LocalDate validade,
    @NotBlank(message = "cvv é obrigatórios")
    String cvv
) {}
