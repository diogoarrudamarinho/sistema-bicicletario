package br.com.vadebicicleta.scb.equipamento.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bicicletas")
@Getter
@Setter
public class Bicicleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = false, nullable = true, updatable = false)
    private UUID publicId;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String modelo;

    @Column(name = "ano_fabricacao", nullable = false)
    private Integer ano;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BicicletaStatus status;
    private Long idTranca;
    private Long idTotem;
}