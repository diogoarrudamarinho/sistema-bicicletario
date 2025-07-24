package br.com.vadebicicleta.scb.equipamento.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrancaIntegrarDTO {
    @NotNull(message = "O ID do totem não pode ser nulo.")
    private UUID idTotem;
    @NotNull(message = "O ID do funcionário não pode ser nulo.")
    private UUID idFuncionario;
}
