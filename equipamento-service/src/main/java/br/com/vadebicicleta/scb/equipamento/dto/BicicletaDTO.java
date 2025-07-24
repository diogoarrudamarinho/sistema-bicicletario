package br.com.vadebicicleta.scb.equipamento.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class BicicletaDTO {
    private UUID id;
    private String marca;
    private String modelo;
    private Integer ano;
    private String status;
    private UUID idTranca;
    private UUID idTotem;
}