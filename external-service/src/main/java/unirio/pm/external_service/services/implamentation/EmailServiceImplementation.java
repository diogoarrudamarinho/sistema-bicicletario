package unirio.pm.external_service.services.implamentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import unirio.pm.external_service.dto.EmailDTO;
import unirio.pm.external_service.exception.email.EmailException;
import unirio.pm.external_service.services.EmailService;

@Service
public class EmailServiceImplementation implements EmailService {
    
    @Autowired
    private JavaMailSender mail;

    @Value("${spring.mail.username}")
    private String remetente;

    @Override
    public String helloWorld(){
        return "Hello world";
    }

    @Override
    public EmailDTO enviarEmail(EmailDTO email) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(remetente);
            mailMessage.setTo(email.getDestinatario());
            mailMessage.setSubject(email.getAssunto());
            mailMessage.setText(email.getMensagem());
            mail.send(mailMessage);
            return email;
        } catch (EmailException ex) {
            throw new EmailException("Erro ao enviar o e-mail: " + ex.getMessage(), ex);
        }
    }
}
