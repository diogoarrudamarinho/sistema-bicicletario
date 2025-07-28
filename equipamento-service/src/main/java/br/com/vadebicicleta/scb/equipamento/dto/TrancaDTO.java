package br.com.vadebicicleta.scb.equipamento.dto;

import lombok.Data;

@Data
public class TrancaDTO {

    private Long id;

    private String numero;

    private String modelo;

    private Integer anoDeFabricacao;

    private String status;

    private Long idTotem; // ID do totem ao qual a tranca está associada

    private Long idBicicleta;
}
