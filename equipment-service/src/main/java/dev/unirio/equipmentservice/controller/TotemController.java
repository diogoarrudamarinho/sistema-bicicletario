package dev.unirio.equipmentservice.controller;

import java.util.List;

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
import dev.unirio.equipmentservice.dto.TotemDTO;
import dev.unirio.equipmentservice.dto.TotemRequestDTO;
import dev.unirio.equipmentservice.dto.TrancaDTO;
import dev.unirio.equipmentservice.service.TotemService;
import dev.unirio.equipmentservice.service.TrancaService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/totem")
public class TotemController {
    
    private final TotemService service;
    private final TrancaService trancaService;

    public TotemController(TotemService service, TrancaService trancaService) {
        this.service = service;
        this.trancaService = trancaService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TotemDTO> getTotem(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.buscarTotem(id));
    }

    @GetMapping()
    public ResponseEntity<List<TotemDTO>> getTotens(){
        return ResponseEntity.status(HttpStatus.OK).body(service.buscarTotens());
    }

    @GetMapping("/{id}/trancas")
    public ResponseEntity<List<TrancaDTO>> getTrancas(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(trancaService.buscarTrancasPorTotem(id));
    }

    @GetMapping("/{id}/bicicletas")
    public ResponseEntity<List<BicicletaDTO>> getBicicletas(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(trancaService.buscarBicicletasPorTotem(id));
    }

    @PostMapping()
    public ResponseEntity<TotemDTO> postTotem(@RequestBody @Valid TotemRequestDTO novoTotem){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarTotem(novoTotem));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TotemDTO> putTotem(@PathVariable Long id, @RequestBody @Valid TotemRequestDTO totem){
        return ResponseEntity.status(HttpStatus.OK).body(service.atualizarTotem(id, totem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTotem(@PathVariable Long id){
        service.deletarTotem(id);
        return ResponseEntity.ok().build();
    }
}
