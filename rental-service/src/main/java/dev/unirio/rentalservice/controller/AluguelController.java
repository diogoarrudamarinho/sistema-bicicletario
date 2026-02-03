package dev.unirio.rentalservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import dev.unirio.rentalservice.service.AluguelService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import dev.unirio.rentalservice.dto.AluguelDTO;
import dev.unirio.rentalservice.dto.AluguelRequestDTO;


@RestController
public class AluguelController {
    
    private final AluguelService service;

    public AluguelController(AluguelService service) {
        this.service = service;
    }

    @PostMapping("/alugar")
    public ResponseEntity<AluguelDTO> alugar(@RequestBody AluguelRequestDTO request) {
        return ResponseEntity.ok().body(service.alugar(request));
    }

    @PostMapping("/devolver")
    public ResponseEntity<AluguelDTO> devolver(@RequestBody AluguelRequestDTO request) {
        return ResponseEntity.ok().body(service.devolver(request));
    }
}
