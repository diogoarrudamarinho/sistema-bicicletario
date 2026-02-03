package dev.unirio.rentalservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.unirio.rentalservice.service.FuncionarioService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import dev.unirio.rentalservice.dto.FuncionarioDTO;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/funcionario")
public class FuncionarioController {
    
    private final FuncionarioService service;

    public FuncionarioController(FuncionarioService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioDTO> buscarFuncionario(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.buscarFuncionario(id));
    }

    @GetMapping()
    public ResponseEntity<List<FuncionarioDTO>> buscarFuncionarios() {
        return ResponseEntity.ok().body(service.buscarFuncionarios());
    }
    
    @PostMapping()
    public ResponseEntity<FuncionarioDTO> criarFuncionario(@RequestBody @Valid FuncionarioDTO dto) {      
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarFuncionario(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioDTO> atualizarFuncionario(@PathVariable Long id, @RequestBody @Valid FuncionarioDTO dto) {
        return ResponseEntity.ok().body(service.atualizarFuncionario(id, dto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFuncionario(@PathVariable Long id) {
        service.deletarFuncionario(id);
        return ResponseEntity.ok().build();
    }
}
