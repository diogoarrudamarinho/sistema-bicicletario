package dev.unirio.rentalservice.entity; 

import java.time.LocalDate;

import dev.unirio.rentalservice.entity.value.Passaporte;
import dev.unirio.rentalservice.enumeration.CiclistaStatus;
import dev.unirio.rentalservice.enumeration.Nacionalidade;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ciclista")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Ciclista {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @OneToOne(
        mappedBy = "ciclista",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Cartao cartao;
    
    @Embedded
    private Passaporte passaporte;

    private CiclistaStatus status = CiclistaStatus.AGUARDANDO_CONFIRMACAO;

    private String nome;
    private LocalDate nascimento;
    private String cpf;
    private Nacionalidade nacionalidade;
    private String email;
    private String senha;
    private String urlFotoDocumento;
}
