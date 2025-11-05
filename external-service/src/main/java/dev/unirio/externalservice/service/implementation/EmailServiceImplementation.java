package dev.unirio.externalservice.service.implementation;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import dev.unirio.externalservice.dto.EmailDTO;
import dev.unirio.externalservice.entity.Email;
import dev.unirio.externalservice.exception.email.EmailException;
import dev.unirio.externalservice.mapper.EmailMapper;
import dev.unirio.externalservice.service.EmailService;

@Service
public class EmailServiceImplementation implements EmailService {
    
    private final EmailMapper mapper;
    private final JavaMailSender mail;

    public EmailServiceImplementation(JavaMailSender mail, EmailMapper mapper) {
        this.mapper = mapper;
        this.mail = mail;
    }

    @Override
    public EmailDTO enviarEmail(EmailDTO email) {
        Email entity = mapper.toEntity(email);
        try {
            var message = new SimpleMailMessage();
            message.setFrom("noreply@email.com");
            message.setTo(entity.getDestinatario());
            message.setSubject(entity.getAssunto());
            message.setText(entity.getMensagem());
            mail.send(message);
            return email;
        } catch (MailException ex) {
            throw new EmailException(ex.getMessage(), ex);
        }
    }
}
