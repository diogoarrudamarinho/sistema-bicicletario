package unirio.pm.external_service.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class CobrancaRequestDTO {
   
    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Valor deve ser maior que zero")
    private BigDecimal valor;

    @NotNull(message = "Ciclista é obrigatório")
    @JsonProperty("idCiclista")
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
