package dev.unirio.externalservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailDTO {
    
    @NotBlank(message = "O destinatário é obrigatório")
    @Email(message = "O e-mail do destinatário está inválido")
    @JsonProperty("email")
    private String destinatario;

    @NotBlank(message = "O assunto é obrigatório")
    private String assunto;

    @NotBlank(message = "O mensagem do e-mail é obrigatório")
    private String mensagem;

    public String getDestinatario() {
        return destinatario;
    }

    public String getAssunto() {
        return assunto;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}