package dev.unirio.equipmentservice.entity;

import dev.unirio.equipmentservice.enumeration.BicicletaStatus;
import dev.unirio.equipmentservice.exception.NegocioException;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="bicicleta")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Bicicleta {
    
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "bicicleta")
    private Tranca tranca;
    
    private String marca;
    private String modelo;
    private String ano;
    private Integer numero;

    @Enumerated(EnumType.STRING)
    private BicicletaStatus status;

    public Bicicleta(){
        // Construtior vazio
    }

    public Bicicleta(String marca, String modelo, String ano, Integer numero, BicicletaStatus status) {
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.numero = numero;
        this.status = status;
    }

    public void alterarStatus(BicicletaStatus novoStatus) {

        if (novoStatus == BicicletaStatus.EM_USO &&
            this.status == BicicletaStatus.REPARO_SOLICITADO) {
            throw new NegocioException("Bicicleta com defeito");
        }

        this.status = novoStatus;
    }
}
