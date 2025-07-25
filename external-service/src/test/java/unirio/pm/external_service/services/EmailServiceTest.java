package unirio.pm.external_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import unirio.pm.external_service.dto.EmailDTO;
import unirio.pm.external_service.entity.Email;
import unirio.pm.external_service.exception.email.EmailException;
import unirio.pm.external_service.mapper.EmailMapper;
import unirio.pm.external_service.services.implamentation.EmailServiceImplementation;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mail;

    @Mock
    private EmailMapper mapper;

    @InjectMocks
    private EmailServiceImplementation service;

    private EmailDTO dto;
    private Email entity;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        dto = new EmailDTO();
        dto.setDestinatario("teste@exemplo.com");
        dto.setAssunto("Assunto");
        dto.setMensagem("Mensagem");

        entity = new Email("teste@exemplo.com", "Assunto", "Mensagem");
    }

    @Test
    @DisplayName("Should return 'Hello world' when is called")
    void helloWorld() {
        String resp = service.helloWorld();
        assertEquals("Hello world", resp);
    }

    @Test
    @DisplayName("Should send email successfully")
    void enviarEmailSuccess() {
        when(mapper.toEntity(dto)).thenReturn(entity);
        doNothing().when(mail).send(any(SimpleMailMessage.class));

        EmailDTO resp = service.enviarEmail(dto);

        assertEquals(dto, resp);
        verify(mail).send(any(SimpleMailMessage.class));
        verify(mapper).toEntity(dto);
    }

    @Test
    @DisplayName("Should throw EmailException on failure")
    void enviarEmailFail() {
        when(mapper.toEntity(dto)).thenReturn(entity);
        doThrow(new EmailException("Falha no envio", new RuntimeException()))
            .when(mail)
            .send(any(SimpleMailMessage.class));

        EmailException ex = assertThrows(EmailException.class, () -> {
            service.enviarEmail(dto);
        });

        assertTrue(ex.getMessage().contains("Falha no envio"));
        verify(mail).send(any(SimpleMailMessage.class));
        verify(mapper).toEntity(dto);
    }
}
