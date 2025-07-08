package unirio.pm.external_service.services.implamentation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import unirio.pm.external_service.dto.EmailDTO;
import unirio.pm.external_service.entity.Email;
import unirio.pm.external_service.exception.email.EmailException;
import unirio.pm.external_service.mapper.EmailMapper;
import unirio.pm.external_service.services.EmailService;

@Service
public class EmailServiceImplementation implements EmailService {
    
    private final EmailMapper mapper;
    private final JavaMailSender mail;

    @Value("${spring.mail.username}")
    private String remetente;

    public EmailServiceImplementation(JavaMailSender mail, EmailMapper mapper) {
        this.mapper = mapper;
        this.mail = mail;
    }

    @Override
    public String helloWorld(){
        return "Hello world";
    }

    @Override
    public EmailDTO enviarEmail(EmailDTO email) {
        Email entity = mapper.toEntity(email);
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(remetente);
            mailMessage.setTo(entity.getDestinatario());
            mailMessage.setSubject(entity.getAssunto());
            mailMessage.setText(entity.getMensagem());
            mail.send(mailMessage);
            return email;
        } catch (EmailException ex) {
            throw new EmailException("Erro ao enviar o e-mail: " + ex.getMessage(), ex);
        }
    }
}
