package unirio.pm.external_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Classe DTO para representar os dados 
 * do cartão de crédito.
 * <br>
 * Esses dados serão validados por uma API externa
 */

public class CartaoDTO {

    @NotBlank(message = "Titular não pode ficar em branco")
    private String titular;

    @NotBlank(message = "Número não pode ficar em branco")
    @Pattern(
        regexp = "^(?:\\d{16}|(?:\\d{4} ){3}\\d{4})$",
        message = "Número de cartão deve ter 16 dígitos, opcionalmente formatado como \"xxxx xxxx xxxx xxxx\""
    )
    private String numero;

    @NotNull(message = "Validade não pode ser nula")
    private String validade;

    @NotBlank(message = "CVV não pode ficar em branco")
    @Size(min = 3, max = 3, message = "CVV deve ter 3 dígitos")
    @Pattern(
        regexp = "\\d{3}", 
        message = "CVV deve conter somente dígitos"
    )
    private String cvv;

    public CartaoDTO() { }

    public String getTitular() {
        return titular;
    }

    public String getNumero() {
        return numero;
    }

    public String getValidade() {
        return validade;
    }

    public String getCvv() {
        return cvv;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    

}