package unirio.pm.external_service.services.implamentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import unirio.pm.external_service.dto.CartaoDTO;
import unirio.pm.external_service.dto.CobrancaDTO;
import unirio.pm.external_service.dto.EmailDTO;
import unirio.pm.external_service.services.Services;

@Service
public class ServiceImplementation implements Services {
    
    @Autowired
    private JavaMailSender mail;

    @Value("${spring.mail.username}")
    private String remetente;

    @Override
    public String helloWorld(){
        return "Hello world";
    }

    @Override
    public void restaurarBanco() {
        throw new UnsupportedOperationException("Not supported yet.");
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
        } catch (Exception e) {
        }

        return email;
    }

    @Override
    public CobrancaDTO criarCobranca(CobrancaDTO cobranca) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void validarCartao(CartaoDTO cartao) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
