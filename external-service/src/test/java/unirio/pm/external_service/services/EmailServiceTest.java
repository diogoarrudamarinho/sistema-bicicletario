package unirio.pm.external_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import unirio.pm.external_service.services.implamentation.EmailServiceImplementation;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
   
    @Mock
    private JavaMailSender mail;

    @InjectMocks
    private EmailServiceImplementation service;
    
    @Test
    @DisplayName("Should return 'Hello world' when is called")
    void helloWorld() {
        String resp = service.helloWorld();
        assertEquals("Hello world", resp);
    }

    @Test
    @DisplayName("Should send email")
    void enviarEmailSuccess(){
        EmailDTO dto = new EmailDTO();

        EmailDTO resp = service.enviarEmail(dto);

        assertEquals(dto, resp);
        verify(mail).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Should throw Exception")
    void enviarEmailFail(){

        EmailDTO dto = new EmailDTO();

        doThrow(new RuntimeException()).when(mail).send(any(SimpleMailMessage.class));

        EmailDTO resp = service.enviarEmail(dto);

        assertNull(resp);
        verify(mail).send(any(SimpleMailMessage.class));
    }
}
