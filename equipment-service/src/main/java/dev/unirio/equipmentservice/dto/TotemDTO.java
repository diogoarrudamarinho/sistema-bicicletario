package dev.unirio.equipmentservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TotemDTO {
    
    private Long id;
    private String localizacao;
    private String descricao;

    public TotemDTO(){
        // Construtor vazio
    }

    public TotemDTO(Long id, String localizacao, String descricao){
        this.id = id;
        this.localizacao = localizacao;
        this.descricao = descricao;
    }
}
