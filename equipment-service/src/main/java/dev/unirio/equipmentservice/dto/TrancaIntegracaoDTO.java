package dev.unirio.equipmentservice.dto;

import dev.unirio.equipmentservice.enumeration.TrancaStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrancaIntegracaoDTO {
    
    @NotNull(message = "Id da tranca é necessário")
    private Long tranca;

    private Long totem;

    @NotNull(message = "Id do funcionário é necessário")
    private Long funcionario;
    private TrancaStatus status;

}
