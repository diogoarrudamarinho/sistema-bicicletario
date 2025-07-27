package br.com.vadebicicleta.scb.equipamento.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrancaIntegrarDTO {
    @NotNull(message = "O ID do totem não pode ser nulo.")
    private UUID idTotem;
    @NotNull(message = "O ID do funcionário não pode ser nulo.")
    private UUID idFuncionario;
}
