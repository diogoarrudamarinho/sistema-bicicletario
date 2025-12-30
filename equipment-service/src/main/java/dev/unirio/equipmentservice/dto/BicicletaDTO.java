package dev.unirio.equipmentservice.dto;

import dev.unirio.equipmentservice.enumeration.BicicletaStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BicicletaDTO {
    
    private Long id;
    private String marca;
    private String modelo;
    private String ano;
    private Integer numero;
    private BicicletaStatus status;
}
