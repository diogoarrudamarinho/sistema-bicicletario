package br.com.vadebicicleta.scb.equipamento.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class TrancaDTO {

    private UUID id;

    private String numero;

    private String modelo;

    private Integer anoDeFabricacao;

    private String status;

    private UUID idTotem; // ID público do totem ao qual a tranca está associada
}
