package dev.unirio.externalservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import dev.unirio.externalservice.dto.CartaoDTO;
import dev.unirio.externalservice.service.CartaoService;

@ExtendWith(MockitoExtension.class)
class CartaoControllerTest {
    
    @Mock
    private CartaoService service; 

    @InjectMocks
    private CartaoController controller; 

    @Test
    @DisplayName("validarCartao: Deve retornar 200 OK e chamar o serviço de validação")
    void testValidarCartao() {
        
        CartaoDTO cartaoDTO = new CartaoDTO("Teste", "1234567890123456", "12/2030", "123");
        
        doNothing().when(service).validarCartao(cartaoDTO); 

        ResponseEntity<Void> response = controller.validarCartao(cartaoDTO);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(service).validarCartao(cartaoDTO);
    }
}
