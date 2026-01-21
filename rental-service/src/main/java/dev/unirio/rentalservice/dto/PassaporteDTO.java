package dev.unirio.rentalservice.dto;

import jakarta.validation.constraints.NotBlank;

public record PassaporteDTO(
    @NotBlank(message = "Número do passaporte é obrigatório")
    String numero,
    
    @NotBlank(message = "País emissor é obrigatório")
    String pais,
    
    @NotBlank(message = "Validade é obrigatória")
    String validade
) {}
