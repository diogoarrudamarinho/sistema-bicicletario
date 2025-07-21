package unirio.pm.external_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import unirio.pm.external_service.dto.CartaoDTO;
import unirio.pm.external_service.services.CartaoService;


@RestController
public class CartaoController {
    
    private final CartaoService service;

    public CartaoController(CartaoService service) {
        this.service = service;
    }

    @PostMapping("/validaCartaoDeCredito")
    public ResponseEntity<Void> validarCartao(@RequestBody @Valid CartaoDTO cartao) {
        service.validarCartao(cartao);
        return ResponseEntity.ok().build();
    }
    
}
