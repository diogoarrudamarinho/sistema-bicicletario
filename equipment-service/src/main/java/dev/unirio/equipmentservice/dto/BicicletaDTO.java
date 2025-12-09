package dev.unirio.equipmentservice.dto;

import dev.unirio.equipmentservice.enumeration.BicicletaStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BicicletaDTO {
    
    private Long id;
    private String marca;
    private String modelo;
    private String ano;
    private Integer numero;
    private BicicletaStatus status;

    public BicicletaDTO(){
        // Construtor vazio
    }

    public BicicletaDTO(Long id, String marca, String modelo, String ano, 
                        Integer numero, BicicletaStatus status) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.numero = numero;
        this.status = status;
    }
}
