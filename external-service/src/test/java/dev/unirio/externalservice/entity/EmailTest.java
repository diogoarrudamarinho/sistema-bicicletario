package dev.unirio.externalservice.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmailTest {
    
    @Test
    @DisplayName("Deve criar um Email e validar suas propriedades")
    void testCriarEmail() {
        String destinatario = "test@example.com";
        String assunto = "Test Subject";
        String corpo = "Test Body";

        Email email = new Email(destinatario, assunto, corpo);

        assertNotNull(email);
        assertEquals(destinatario, email.getDestinatario());
        assertEquals(assunto, email.getAssunto());
        assertEquals(corpo, email.getMensagem());
    }

    @Test
    @DisplayName("Deve trocar e recuperar as informações corretas")
    void testSettersAndGetters() {
        Email email = new Email();

        email.setDestinatario("new@example.com");
        email.setAssunto("New Subject");
        email.setMensagem("New Body");

        assertEquals("new@example.com", email.getDestinatario());
        assertEquals("New Subject", email.getAssunto());
        assertEquals("New Body", email.getMensagem());
    }

}
