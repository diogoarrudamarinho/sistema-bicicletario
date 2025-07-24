package br.com.vadebicicleta.scb.equipamento.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class RetirarDaRedeDTO {

    @NotNull(message = "O ID do funcionário não pode ser nulo.")
    private UUID idFuncionario;

    @NotNull(message = "O ID da bicicleta não pode ser nulo.")
    private UUID idBicicleta;

    @NotNull(message = "O ID da tranca não pode ser nulo.")
    private UUID idTranca;

    @NotBlank(message = "O status da ação não pode ser em branco. Use 'EM_REPARO' ou 'APOSENTADA'.")
    private String statusAcaoReparador;
}
