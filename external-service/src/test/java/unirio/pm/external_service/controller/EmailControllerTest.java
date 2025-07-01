package unirio.pm.external_service.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import unirio.pm.external_service.dto.EmailDTO;
import unirio.pm.external_service.services.EmailService;

@ExtendWith(MockitoExtension.class)
public class EmailControllerTest {

    @InjectMocks
    private EmailController controller;

    @Mock
    private EmailService service;

    @Test
    @DisplayName("Should return 'Hello World'")
    public void getHello(){
        when(service.helloWorld()).thenReturn("Hello World");

        String resp = controller.getHello();

        assertEquals("Hello World", resp);
        verify(service).helloWorld();
    }

    @Test
    @DisplayName("Should return ok")
    public void testEnviarEmail(){
        EmailDTO email = new EmailDTO();

        when(service.enviarEmail(email)).thenReturn(email);

        ResponseEntity<EmailDTO> resp = controller.postEmail(email);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(email, resp.getBody());
        verify(service).enviarEmail(email);
    }
}