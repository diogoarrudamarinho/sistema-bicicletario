package dev.unirio.externalservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.unirio.externalservice.dto.CartaoDTO;
import dev.unirio.externalservice.service.CartaoService;
import jakarta.validation.Valid;

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
