package br.com.vadebicicleta.scb.equipamento.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "trancas")
@Getter
@Setter
public class Tranca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private UUID publicId;

    private String numero;

    @Column(nullable = false)
    private String modelo;

    @Column(name = "ano_de_fabricacao", nullable = false)
    private Integer anoDeFabricacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrancaStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "totem_id")
    private Totem totem;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bicicleta_id", unique = true)
    private Bicicleta bicicleta;
}