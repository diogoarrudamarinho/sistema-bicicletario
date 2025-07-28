package br.com.vadebicicleta.scb.equipamento.dto;

import lombok.Data;

@Data
public class BicicletaDTO {
    private Long id;
    private String marca;
    private String modelo;
    private Integer ano;
    private String status;
    private Long idTranca;
    private Long idTotem;
}