package dev.unirio.rentalservice.dto;

import dev.unirio.rentalservice.enumeration.TrancaStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrancaDTO {
    
    private Long id;
    private Long bicicleta;
    private Integer numero;
    private String localizacao;
    private String anoDeFabricacao;
    private String modelo;
    private TrancaStatus status;

}
