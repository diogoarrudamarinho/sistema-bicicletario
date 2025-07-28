package br.com.vadebicicleta.scb.equipamento.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IntegrarNaRedeDTO {

    @NotNull(message = "O ID do funcionário não pode ser nulo.")
    private Long idFuncionario;

    @NotNull(message = "O ID da bicicleta não pode ser nulo.")
    private Long idBicicleta;

    @NotNull(message = "O ID da tranca não pode ser nulo.")
    private Long idTranca;
}
