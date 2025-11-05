package dev.unirio.externalservice.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmailDTOTest {
    
    @Test
    @DisplayName("Deve criar um EmailDTO e validar as suas propriedades")
    void testCriarEmailDTO() {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setDestinatario("test@example.com");
        emailDTO.setAssunto("Test Subject");
        emailDTO.setMensagem("Test Body");

        assertNotNull(emailDTO);
        assertEquals("test@example.com", emailDTO.getDestinatario());
        assertEquals("Test Subject", emailDTO.getAssunto());
        assertEquals("Test Body", emailDTO.getMensagem());
    }
}
