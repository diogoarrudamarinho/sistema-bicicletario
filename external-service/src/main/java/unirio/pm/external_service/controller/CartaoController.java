package unirio.pm.external_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import unirio.pm.external_service.dto.CartaoDTO;
import unirio.pm.external_service.services.CartaoService;


@RestController
@RequestMapping("/cartao")
public class CartaoController {
    
    @Autowired
    CartaoService service;

    @PostMapping("/validar")
    public ResponseEntity<Boolean> vaalidarCartao(@RequestBody @Valid CartaoDTO cartao) {
        return ResponseEntity.ok(service.validarCartao(cartao));
    }
    
}
