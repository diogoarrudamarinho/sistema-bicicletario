package br.com.vadebicicleta.scb.equipamento.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NovoTotemDTO {

    @NotBlank(message = "A localização não pode ser em branco.")
    private String localizacao;

    private String descricao;
}
