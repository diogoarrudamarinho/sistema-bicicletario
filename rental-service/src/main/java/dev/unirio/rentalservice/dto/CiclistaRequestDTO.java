package dev.unirio.rentalservice.dto;

import java.time.LocalDate;

import dev.unirio.rentalservice.enumeration.Nacionalidade;
import jakarta.validation.constraints.NotNull;

public record CiclistaRequestDTO(
    String nome,
    String email,
    String cpf,
    String senha,
    @NotNull(message = "Nacionalidade é Obrigatório")
    Nacionalidade nacionalidade,
    LocalDate nascimento,
    PassaporteDTO passaporte,
    String urlFotoDocumento,
    @NotNull(message = "Cartão é Obrigatório")
    CartaoDTO cartao
) {}
