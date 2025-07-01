package unirio.pm.external_service.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)public class EmailDTOTest {
    
    @Test
    @DisplayName("Should create EmailDTO and validate its properties")
    public void testEmailDTO() {
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
