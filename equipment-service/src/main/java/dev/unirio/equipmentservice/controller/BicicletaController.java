package dev.unirio.equipmentservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.unirio.equipmentservice.dto.BicicletaDTO;
import dev.unirio.equipmentservice.dto.BicicletaRequestDTO;
import dev.unirio.equipmentservice.service.BicicletaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/bicicleta")
public class BicicletaController {
    
    private final BicicletaService service;

    public BicicletaController(BicicletaService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BicicletaDTO> getBicicleta(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.buscarBicicleta(id));
    }    

    @PostMapping()
    public ResponseEntity<BicicletaDTO> postBicicleta(@RequestBody @Valid BicicletaRequestDTO bicicleta) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarBicicleta(bicicleta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BicicletaDTO> putBicicleta(@PathVariable Long id, @RequestBody @Valid BicicletaRequestDTO bicicleta) {
        return ResponseEntity.status(HttpStatus.OK).body(service.atualizarBicicleta(id, bicicleta));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delBicicleta(@PathVariable Long id){
        service.deletarBicicleta(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
