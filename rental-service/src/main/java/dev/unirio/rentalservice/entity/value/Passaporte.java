package dev.unirio.rentalservice.entity.value;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Passaporte {
    
    private String nacionalidade;
    private String email;
    private String urlFotoDocumento;
    private String senha;
}
