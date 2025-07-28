package br.com.vadebicicleta.scb.equipamento.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrancaRetirarDTO {
    @NotNull(message = "O ID do funcionário não pode ser nulo.")
    private Long idFuncionario;
    @NotBlank(message = "A ação (status) não pode ser em branco.")
    private String statusAcaoReparador;
}