package unirio.pm.external_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import unirio.pm.external_service.enumerations.StatusCobranca;

public class CobrancaDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Valor é obrigatório")
    private BigDecimal valor;

    @NotNull(message = "Ciclista é obrigatório")
    private Long ciclista;
    
    @Enumerated(EnumType.STRING)
    private StatusCobranca status;
    
    private LocalDate horaSolicitacao;
    private LocalDate horaFinalizacao;

    public CobrancaDTO() {}

    public CobrancaDTO(BigDecimal valor, Long ciclista) {
        this.valor = valor;
        this.ciclista = ciclista;
        this.status = StatusCobranca.PENDENTE; 
        this.horaSolicitacao = LocalDate.now(); 
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
