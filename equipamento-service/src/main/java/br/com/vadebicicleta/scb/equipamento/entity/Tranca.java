package br.com.vadebicicleta.scb.equipamento.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "trancas")
@Getter
@Setter
public class Tranca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = false, nullable = true, updatable = false)
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