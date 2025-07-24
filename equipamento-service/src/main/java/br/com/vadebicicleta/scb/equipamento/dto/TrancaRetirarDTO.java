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
public class TrancaRetirarDTO {
    @NotNull(message = "O ID do funcionário não pode ser nulo.")
    private UUID idFuncionario;
    @NotBlank(message = "A ação (status) não pode ser em branco.")
    private String statusAcaoReparador;
}