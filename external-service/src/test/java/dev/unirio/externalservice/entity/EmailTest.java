package dev.unirio.externalservice.entity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
        email.setId(1L);

        assertEquals(1L, email.getId());
        assertEquals("new@example.com", email.getDestinatario());
        assertEquals("New Subject", email.getAssunto());
        assertEquals("New Body", email.getMensagem());
    }

    @Test
    @DisplayName("Deve validar o hashcode e equals")
    void testHashCodeAndEquals() {
        Email email1 = new Email("test@example.com", "Subject 1", "Body 1");
        email1.setId(1L);

        Email email2 = new Email("test@example.com", "Subject 2", "Body 2");
        email2.setId(1L);

        Email email3 = new Email("other@example.com", "Subject 3", "Body 3");
        email3.setId(2L);

        Email email4 = null;

        List<Integer> list = new ArrayList<>();

        assertEquals(email1, email1);
        assertEquals(email1, email2); 
        assertNotEquals(email1, email3); 
        assertNotEquals(email1, list); 
        assertNotEquals(email1, email4);

        assertEquals(email1.hashCode(), email2.hashCode()); 
        assertNotEquals(email1.hashCode(), email3.hashCode()); 
    }
}
