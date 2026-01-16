package dev.unirio.equipmentservice.dto;

import dev.unirio.equipmentservice.enumeration.BicicletaStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BicicletaIntegracaoDTO {
    
    @NotNull(message = "ID da Bicicleta é obrigatório")
    private Long bicicleta;

    @NotNull(message = "ID do Funcionário é obrigatório")
    private Long funcionario;

    private Long tranca;
    private BicicletaStatus status;
    
}
