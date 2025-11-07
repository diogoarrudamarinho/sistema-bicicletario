package dev.unirio.externalservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import dev.unirio.externalservice.enumeration.StatusCobranca;

public class CobrancaDTO {
    
    private Long id;
    private BigDecimal valor;
    private Long ciclista;
    private StatusCobranca status;
    private LocalDateTime horaSolicitacao;
    private LocalDateTime horaFinalizacao;

    public CobrancaDTO(){
    }

    public CobrancaDTO( Long id, BigDecimal valor, Long ciclista, 
                        StatusCobranca status, LocalDateTime horaSolicitacao,
                        LocalDateTime horaFinalizacao) {
        this.id = id;
        this.valor = valor;
        this.ciclista = ciclista;
        this.status = status;
        this.horaSolicitacao = horaSolicitacao;
        this.horaFinalizacao = horaFinalizacao;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public Long getCiclista() {
        return ciclista;
    }

    public StatusCobranca getStatus() {
        return status;
    }

    public LocalDateTime getHoraSolicitacao() {
        return horaSolicitacao;
    }

    public LocalDateTime getHoraFinalizacao() {
        return horaFinalizacao;
    }
}
