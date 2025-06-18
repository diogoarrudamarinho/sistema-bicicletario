package unirio.pm.external_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import unirio.pm.external_service.enumerations.StatusCobranca;

public class CobrancaDTO {

    private Long id;
    private BigDecimal valor;
    private Long ciclista;
    private StatusCobranca status;
    private LocalDate horaSolicitacao;
    private LocalDate horaFinalizacao;

    public CobrancaDTO(){
    }

    public CobrancaDTO(Long id, BigDecimal valor, Long ciclista, StatusCobranca status, LocalDate horaSolicitacao, LocalDate horaFinalizacao) {
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

    public LocalDate getHoraSolicitacao() {
        return horaSolicitacao;
    }

    public LocalDate getHoraFinalizacao() {
        return horaFinalizacao;
    }
}
