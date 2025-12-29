package dev.unirio.equipmentservice.dto;

import dev.unirio.equipmentservice.enumeration.TrancaStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrancaRequestDTO {
    
    @NotNull(message = "Tranca necessita de um número")
    @DecimalMin(value = "0.0", inclusive = false, message = "Valor deve ser maior que zero")
    private Integer numero;

    private String localizacao;
    private String anoDeFabricacao;
    private String modelo;
    private TrancaStatus status;

    public TrancaRequestDTO(){
        // Construtor vazio
    }

    public TrancaRequestDTO(String anoDeFabricacao, String localizacao, String modelo, Integer numero, TrancaStatus status) {
        this.anoDeFabricacao = anoDeFabricacao;
        this.localizacao = localizacao;
        this.modelo = modelo;
        this.numero = numero;
        this.status = status;
    }

}
