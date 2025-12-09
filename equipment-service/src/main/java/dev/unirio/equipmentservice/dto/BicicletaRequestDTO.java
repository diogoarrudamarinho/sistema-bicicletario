package dev.unirio.equipmentservice.dto;

import dev.unirio.equipmentservice.enumeration.BicicletaStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BicicletaRequestDTO {
    
    @NotBlank(message = "Status da bicicleta é obrigatório")
    private String marca;
    
    @NotBlank(message = "Status da bicicleta é obrigatório")
    private String modelo;

    @NotBlank(message = "Status da bicicleta é obrigatório")
    private String ano;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Valor deve ser maior que zero")
    private Integer numero;

    @NotBlank(message = "Status da bicicleta é obrigatório")
    private BicicletaStatus status;

    public BicicletaRequestDTO(){
        // Construtor vazio
    }

    public BicicletaRequestDTO( String marca, String modelo, String ano, 
                                Integer numero, BicicletaStatus status) {
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.numero = numero;
        this.status = status;
    }
}
