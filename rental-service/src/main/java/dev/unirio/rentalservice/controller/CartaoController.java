package dev.unirio.rentalservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.unirio.rentalservice.service.CartaoService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import dev.unirio.rentalservice.dto.CartaoDTO;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/cartaoDeCredito")
public class CartaoController {
    
    public final CartaoService service;

    public CartaoController(CartaoService service) {
        this.service = service;
    }

    @GetMapping("/{idCiclista}")
    public ResponseEntity<CartaoDTO> buscarCartao(@PathVariable Long idCiclista) {
        return ResponseEntity.ok().body(service.buscarCartao(idCiclista));
    }

    @PutMapping("/{idCiclista}")
    public ResponseEntity<CartaoDTO> atualizarCartao(@PathVariable Long idCiclista, @RequestBody @Valid CartaoDTO dto) {
        return ResponseEntity.ok().body(service.atualizarCartao(idCiclista, dto));
    }
}
