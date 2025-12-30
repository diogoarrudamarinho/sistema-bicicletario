package dev.unirio.equipmentservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TotemRequestDTO {
    
    @NotBlank(message = "Localzação não pode estar vazia")
    private String localizacao;
    private String descricao;
}
