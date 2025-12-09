package dev.unirio.equipmentservice.dto;

import dev.unirio.equipmentservice.enumeration.BicicletaStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public BicicletaStatus getStatus() {
        return status;
    }

    public void setStatus(BicicletaStatus status) {
        this.status = status;
    }
}
