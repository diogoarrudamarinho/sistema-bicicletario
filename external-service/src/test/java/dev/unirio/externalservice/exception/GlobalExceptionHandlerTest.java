package dev.unirio.externalservice.exception;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import dev.unirio.externalservice.exception.email.EmailException;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {
    
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("Deve lançar exceptions de validação")
    void testHandleValidationExceptions() {
        ObjectError error1 = new ObjectError("field1", "Campo obrigatório");
        ObjectError error2 = new ObjectError("field2", "Valor inválido");

        var bindingResult = mock(org.springframework.validation.BindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(List.of(error1, error2));

        var ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<List<String>> response = handler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals(List.of("Campo obrigatório", "Valor inválido"), response.getBody());
    }

    @Test
    @DisplayName("Deve retornar 500 e o formato de erro esperado ao lançar EmailException")
    void handleEmailExceptionsTest() {
        String mensagem = "falha";
        Throwable cause = new MailSendException("error");
        EmailException emailException = new EmailException(mensagem, cause);

        ResponseEntity<Map<String, Object>> response = handler.handleEmailExceptions(emailException);
       
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseBody.get("status"));
        assertEquals("Erro ao enviar e-mail", responseBody.get("error"));
        assertEquals(mensagem, responseBody.get("message"));
    }

    @Test
    @DisplayName("Deve retornar 404 (NOT_FOUND) e a mensagem de erro para ObjectNotFoundException")
    void testHandleObjectNotFoundException() {
        
        String mensagemErro = "Ciclista com ID 999 não encontrado.";
        Long id = 1L;

        ObjectNotFoundException notFoundException = new ObjectNotFoundException(mensagemErro, id);

        ResponseEntity<Map<String, Object>> response = handler.handleObjectNotFoundException(notFoundException);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
