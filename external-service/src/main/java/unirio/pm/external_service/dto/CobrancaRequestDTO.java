package unirio.pm.external_service.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public class CobrancaRequestDTO {
   
    @NotNull(message = "Valor é obrigatório")
    private BigDecimal valor;

    @NotNull(message = "Ciclista é obrigatório")
    private Long ciclista;

    public CobrancaRequestDTO() {}

    public BigDecimal getValor() {
        return valor;
    }

    public Long getCiclista() {
        return ciclista;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public void setCiclista(Long ciclista) {
        this.ciclista = ciclista;
    }
}
