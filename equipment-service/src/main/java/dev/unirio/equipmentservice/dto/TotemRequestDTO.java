package dev.unirio.equipmentservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TotemRequestDTO {
    
    @NotBlank(message = "Localzação não pode estar vazia")
    public String localizacao;
    public String descricao;

    public TotemRequestDTO(){
        // Construtor vazio
    }

    public TotemRequestDTO(String descricao, String localizacao) {
        this.descricao = descricao;
        this.localizacao = localizacao;
    }
    
}
