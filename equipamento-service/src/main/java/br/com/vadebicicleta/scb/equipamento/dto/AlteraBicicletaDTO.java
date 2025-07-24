package br.com.vadebicicleta.scb.equipamento.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AlteraBicicletaDTO {

    @NotBlank(message = "Marca não pode ser em branco.")
    private String marca;

    @NotBlank(message = "Modelo não pode ser em branco.")
    private String modelo;

    @NotNull(message = "Ano não pode ser nulo.")
    @Positive(message = "Ano deve ser um número positivo.")
    private Integer ano;
}