package unirio.pm.external_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailDTO {
    
    @NotBlank(message = "O destinatário é obrigatório")
    @Email(message = "O e-mail do destinatário está inválido")
    private String destinatario;

    @NotBlank(message = "O assunto é obrigatório")
    private String assunto;

    @NotBlank(message = "O corpo do e-mail é obrigatório")
    private String corpo;

    public EmailDTO() {
    }

    public EmailDTO(String destinatario, String assunto, String corpo) {
        this.destinatario = destinatario;
        this.assunto = assunto;
        this.corpo = corpo;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public String getAssunto() {
        return assunto;
    }

    public String getCorpo() {
        return corpo;
    }
}
