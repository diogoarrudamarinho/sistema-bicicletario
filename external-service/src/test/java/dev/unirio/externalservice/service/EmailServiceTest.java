package dev.unirio.externalservice.service;

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
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import dev.unirio.externalservice.dto.EmailDTO;
import dev.unirio.externalservice.entity.Email;
import dev.unirio.externalservice.exception.email.EmailException;
import dev.unirio.externalservice.mapper.EmailMapper;
import dev.unirio.externalservice.service.implementation.EmailServiceImplementation;

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

    @SuppressWarnings("null")
    @Test
    @DisplayName("Deve enviar o email")
    void enviarEmailSuccess() {
        when(mapper.toEntity(dto)).thenReturn(entity);
        doNothing().when(mail).send(any(SimpleMailMessage.class));

        EmailDTO resp = service.enviarEmail(dto);

        assertEquals(dto, resp);
        verify(mail).send(any(SimpleMailMessage.class));
        verify(mapper).toEntity(dto);
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Deve lançar excepiton")
    void enviarEmailFail() {

        when(mapper.toEntity(dto)).thenReturn(entity);
        doThrow(new MailSendException("erro"))
            .when(mail)
            .send(any(SimpleMailMessage.class));

        EmailException ex = assertThrows(EmailException.class, () -> {
            service.enviarEmail(dto);
        });

        assertTrue(ex.getMessage().contains("erro"));

        verify(mail).send(any(SimpleMailMessage.class));
        verify(mapper).toEntity(dto);
    }
}
