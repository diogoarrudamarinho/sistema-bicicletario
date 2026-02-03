package dev.unirio.rentalservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.unirio.rentalservice.service.CiclistaService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import dev.unirio.rentalservice.dto.BicicletaDTO;
import dev.unirio.rentalservice.dto.CiclistaDTO;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import dev.unirio.rentalservice.dto.CiclistaRequestDTO;


@RestController
@RequestMapping("/ciclista")
public class CiclistaController {
    
    private final CiclistaService service;

    public CiclistaController(CiclistaService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CiclistaDTO> buscarCiclista(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.buscarCiclista(id));
    }
    
    @GetMapping("/{id}/bicicletaAlugada")
    public ResponseEntity<BicicletaDTO> buscarBicicleta(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.buscarBicicleta(id));
    }

    @GetMapping("/{id}/permiteAluguel")
    public ResponseEntity<Boolean> permiteAluguel(@PathVariable Long id) {
        return ResponseEntity.ok(service.permiteAluguel(id));
    }

    @GetMapping("/existeEmail/{email}")
    public ResponseEntity<Boolean> permiteAluguel(@PathVariable String email) {
        return ResponseEntity.ok(service.emailExistente(email));
    }

    @PostMapping("")
    public ResponseEntity<CiclistaDTO> criarCiclista(@RequestBody @Valid CiclistaRequestDTO dto) {        
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarCiclista(dto));
    }

    @PostMapping("/{id}/ativar")
    public ResponseEntity<CiclistaDTO> ativarCadastro(@PathVariable Long id, @RequestParam String token) {        
        return ResponseEntity.ok().body(service.ativarCadastro(id, token));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCiclista(@PathVariable Long id) {
        service.desativarCadastro(id);
        return ResponseEntity.ok().build();
    }
}