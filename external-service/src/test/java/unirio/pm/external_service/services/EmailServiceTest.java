package unirio.pm.external_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import unirio.pm.external_service.dto.EmailDTO;
import unirio.pm.external_service.exception.EmailException;
import unirio.pm.external_service.services.implamentation.EmailServiceImplementation;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
   
    @Mock
    private JavaMailSender mail;

    @InjectMocks
    private EmailServiceImplementation service;
    
    @Test
    @DisplayName("Should return 'Hello world' when is called")
    public void helloWorld() {
        String resp = service.helloWorld();
        assertEquals("Hello world", resp);
    }

    @Test
    @DisplayName("Should send email")
    public void enviarEmailSuccess(){
        EmailDTO dto = new EmailDTO();

        EmailDTO resp = service.enviarEmail(dto);

        assertEquals(dto, resp);
        verify(mail).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Should throw Exception")
    public void enviarEmailFail(){

        EmailDTO dto = new EmailDTO();
        dto.setDestinatario("teste@exemplo.com");
        dto.setAssunto("Assunto");
        dto.setMensagem("Mensagem");

        doThrow(new EmailException("Falha no envio", new RuntimeException()))
            .when(mail)
            .send(any(SimpleMailMessage.class));

        EmailException ex = assertThrows(
            EmailException.class,
            () -> service.enviarEmail(dto),
            "Esperava EmailException em caso de falha no envio"
        );

        assertTrue(ex.getMessage().contains("Falha no envio"));
        verify(mail).send(any(SimpleMailMessage.class));
    }
}
