package dev.unirio.equipmentservice.entity;

import dev.unirio.equipmentservice.enumeration.TrancaStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tranca")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Tranca {
    
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "bicicleta_id", nullable = true)
    private Bicicleta bicicleta;

    @ManyToOne
    @JoinColumn(name = "totem_id")
    private Totem totem;

    @Column(nullable = false)
    private Integer numero;

    private String localizacao;

    private String anoDeFabricacao;

    private String modelo;

    @Enumerated(EnumType.STRING)
    private TrancaStatus status;

    
    public Tranca() {
        // Construtor vazio
    }

    public Tranca(Integer numero, String localizacao, String anoDeFabricacao, String modelo, TrancaStatus status) {
        this.numero = numero;
        this.localizacao = localizacao;
        this.anoDeFabricacao = anoDeFabricacao;
        this.modelo = modelo;
        this.status = status;
    }
}
