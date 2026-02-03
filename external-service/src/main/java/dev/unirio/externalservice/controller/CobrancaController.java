package dev.unirio.externalservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.unirio.externalservice.dto.CobrancaDTO;
import dev.unirio.externalservice.dto.CobrancaRequestDTO;
import dev.unirio.externalservice.service.CobrancaService;
import jakarta.validation.Valid;

@RestController
public class CobrancaController {
    
    private final CobrancaService service;

    public CobrancaController(CobrancaService service) {
        this.service = service;
    }

    @PostMapping("/cobranca")
    public ResponseEntity<CobrancaDTO> criarCobranca(@RequestBody @Valid CobrancaRequestDTO cobranca) {        
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarCobranca(cobranca));
    }

    @PostMapping("/processaCobrancasEmFila")
    public ResponseEntity<List<CobrancaDTO>> processarFila() {
        return ResponseEntity.status(HttpStatus.OK).body(service.processarFilaCobranca());
    }

    @GetMapping("/cobranca/{id}")
    public ResponseEntity<CobrancaDTO> buscarCobranca(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarCobranca(id));
    }

    @DeleteMapping("/cobranca/{id}")
    public ResponseEntity<Void> deletarCobranca(@PathVariable Long id){
        service.deletar(id);
        return ResponseEntity.ok().build();
    }
    
}