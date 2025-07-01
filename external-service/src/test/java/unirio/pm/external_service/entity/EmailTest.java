package unirio.pm.external_service.entity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)public class EmailTest {

    @Test
    @DisplayName("Should create Email and validate its properties")
    public void testEmailCreation() {
        String destinatario = "test@example.com";
        String assunto = "Test Subject";
        String corpo = "Test Body";

        Email Email = new Email(destinatario, assunto, corpo);

        assertNotNull(Email);
        assertEquals(destinatario, Email.getDestinatario());
        assertEquals(assunto, Email.getAssunto());
        assertEquals(corpo, Email.getMensagem());
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    public void testSettersAndGetters() {
        Email Email = new Email();

        Email.setDestinatario("new@example.com");
        Email.setAssunto("New Subject");
        Email.setMensagem("New Body");
        Email.setId(1L);

        assertEquals(1L, Email.getId());
        assertEquals("new@example.com", Email.getDestinatario());
        assertEquals("New Subject", Email.getAssunto());
        assertEquals("New Body", Email.getMensagem());
    }

    @Test
    @DisplayName("Should validate hashCode and equals methods for Email")
    public void testHashCodeAndEquals() {
        Email email1 = new Email("test@example.com", "Subject 1", "Body 1");
        email1.setId(1L);

        Email email2 = new Email("test@example.com", "Subject 2", "Body 2");
        email2.setId(1L);

        Email email3 = new Email("other@example.com", "Subject 3", "Body 3");
        email3.setId(2L);

        List<Integer> list = new ArrayList<>();

        assertEquals(email1, email1);
        assertEquals(email1, email2); 
        assertNotEquals(email1, email3); 
        assertNotEquals(email1, null); 
        assertNotEquals(email1, list); 



        assertEquals(email1.hashCode(), email2.hashCode()); 
        assertNotEquals(email1.hashCode(), email3.hashCode()); 
    }
}
