package dev.unirio.externalservice.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class CobrancaRequestDTO {

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Valor deve ser maior que zero")
    private BigDecimal valor;

    @NotNull(message = "Ciclista é obrigatório")
    private Long ciclista;

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
