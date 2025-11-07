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

@Entity
@Table(name = "cobrancas")
public class Cobranca {
     
    @Id
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
        // Construtor vazio
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Cobranca other = (Cobranca) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
