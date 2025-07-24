package br.com.vadebicicleta.scb.equipamento.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class TotemDTO {

    private UUID id;

    private String localizacao;

    private String descricao;
}
