package dev.unirio.externalservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import dev.unirio.externalservice.dto.EmailDTO;
import dev.unirio.externalservice.entity.Email;
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
    @DisplayName("Should send email successfully")
    void enviarEmailSuccess() {
        when(mapper.toEntity(dto)).thenReturn(entity);
        doNothing().when(mail).send(any(SimpleMailMessage.class));

        EmailDTO resp = service.enviarEmail(dto);

        assertEquals(dto, resp);
        verify(mail).send(any(SimpleMailMessage.class));
        verify(mapper).toEntity(dto);
    }
}
