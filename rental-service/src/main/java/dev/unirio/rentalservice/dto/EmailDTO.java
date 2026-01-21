package dev.unirio.rentalservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailDTO(
    @NotBlank(message = "O destinatário é obrigatório")
    @Email(message = "O e-mail do destinatário está inválido")
    @JsonProperty("email")
    String destinatario,

    @NotBlank(message = "O assunto é obrigatório")
    String assunto,

    @NotBlank(message = "O mensagem do e-mail é obrigatório")
    String mensagem
){}