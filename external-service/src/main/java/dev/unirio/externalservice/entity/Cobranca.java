package dev.unirio.externalservice.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import dev.unirio.externalservice.enumeration.StatusCobranca;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "cobranca")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cobranca {
     
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal valor;

    @Column(name = "ciclista_id", nullable = false)
    private Long ciclista;
    
    @Enumerated(EnumType.STRING)
    private StatusCobranca status;
    
    @Column(name = "hora_solicitacao", nullable = false)
    private LocalDateTime horaSolicitacao;

    @Column(name = "hora_finalizacao")
    private LocalDateTime horaFinalizacao;

    public Cobranca(BigDecimal valor, Long ciclista) {
        this.valor = valor;
        this.ciclista = ciclista;
        this.horaSolicitacao = LocalDateTime.now();
        this.status = StatusCobranca.PENDENTE; 
    }

    public Cobranca(){
        this.horaSolicitacao = LocalDateTime.now();
        this.status = StatusCobranca.PENDENTE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Long getCiclista() {
        return ciclista;
    }

    public void setCiclista(Long ciclista) {
        this.ciclista = ciclista;
    }

    public StatusCobranca getStatus() {
        return status;
    }

    public void setStatus(StatusCobranca status) {
        this.status = status;
    }

    public LocalDateTime getHoraSolicitacao() {
        return horaSolicitacao;
    }

    public void setHoraSolicitacao(LocalDateTime horaSolicitacao) {
        this.horaSolicitacao = horaSolicitacao;
    }

    public LocalDateTime getHoraFinalizacao() {
        return horaFinalizacao;
    }

    public void setHoraFinalizacao(LocalDateTime horaFinalizacao) {
        this.horaFinalizacao = horaFinalizacao;
    }
}
