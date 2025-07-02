package unirio.pm.external_service.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import unirio.pm.external_service.dto.CartaoDTO;
import unirio.pm.external_service.services.CartaoService;

@ExtendWith(MockitoExtension.class)
public class CartaoControllerTest {
    
    @InjectMocks
    private CartaoController controller;

    @Mock
    private CartaoService service;

    @Test
    @DisplayName("Deve validar o cartão")
    void testValidaCartao() {

        CartaoDTO cartao = new CartaoDTO();
        ResponseEntity<Void> response = controller.validarCartao(cartao);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(service).validarCartao(cartao);
    }
}
