package dev.unirio.externalservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CartaoDTO {
    
    @NotBlank(message = "Titular não pode ficar em branco")
    @JsonProperty("nomeTitular")
    private String titular;

    @NotBlank(message = "Número não pode ficar em branco")
    @Pattern(
        regexp = "^(?:\\d{16}|(?:\\d{4} ){3}\\d{4})$",
        message = "Número de cartão deve ter 16 dígitos"
    )
    private String numero;

    @NotBlank(message = "Validade não pode ser nula")
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$", 
             message = "O formato da validade deve ser YYYY-MM. Ex: 2027-08")
    private String validade;

    @NotBlank(message = "CVV não pode ficar em branco")
    @Size(min = 3, max = 3, message = "CVV deve ter 3 dígitos")
    @Pattern(
        regexp = "\\d{3}", 
        message = "CVV deve conter somente dígitos"
    )
    private String cvv;

    public CartaoDTO() { }

    public CartaoDTO(String titular, String numero, String validade, String cvv) {
        this.titular = titular;
        this.numero = numero;
        this.validade = validade;
        this.cvv = cvv;
    }

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
