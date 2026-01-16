package dev.unirio.equipmentservice.dto;

import dev.unirio.equipmentservice.enumeration.TrancaStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrancaRequestDTO {
    
    @NotNull(message = "Tranca necessita de um número")
    @DecimalMin(value = "0.0", inclusive = false, message = "Valor deve ser maior que zero")
    private Integer numero;

    private String localizacao;
    private String anoDeFabricacao;
    private String modelo;

    @NotNull(message = "Status é obrigatório")
    private TrancaStatus status;

}
