package br.com.vadebicicleta.scb.equipamento.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NovaTrancaDTO {

    @NotBlank(message = "O número da tranca não pode ser em branco.")
    private String numero;

    @NotBlank(message = "O modelo não pode ser em branco.")
    private String modelo;

    @NotNull(message = "O ano de fabricação não pode ser nulo.")
    private Integer anoDeFabricacao;
}