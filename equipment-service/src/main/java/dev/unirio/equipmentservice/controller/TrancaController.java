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
import dev.unirio.equipmentservice.dto.TrancaDTO;
import dev.unirio.equipmentservice.dto.TrancaIntegracaoDTO;
import dev.unirio.equipmentservice.dto.TrancaRequestDTO;
import dev.unirio.equipmentservice.enumeration.TrancaStatus;
import dev.unirio.equipmentservice.service.TrancaService;
import jakarta.validation.Valid;




@RestController
@RequestMapping("/tranca")
public class TrancaController {
    
    private final TrancaService service;

    public TrancaController(TrancaService service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<List<TrancaDTO>> getTrancas() {
        return ResponseEntity.status(HttpStatus.OK).body(service.buscarTrancas());
    }
        
    @GetMapping("/{id}")
    public ResponseEntity<TrancaDTO> getTranca(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.buscarTranca(id));
    }

    @GetMapping("/{id}/bicicleta")
    public ResponseEntity<BicicletaDTO> getBicicleta(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.buscarBicicleta(id));
    }
    
    @PostMapping()
    public ResponseEntity<TrancaDTO> postTranca(@RequestBody @Valid TrancaRequestDTO novaTranca) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrarTranca(novaTranca));
    }

    @PostMapping("/{id}/trancar/{bicicletaId}")
    public ResponseEntity<TrancaDTO> postTrancar(@PathVariable Long id, @PathVariable Long bicicletaId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.trancar(id, bicicletaId));
    }

    @PostMapping("/{id}/destrancar/{bicicletaId}")
    public ResponseEntity<TrancaDTO> postDestrancar(@PathVariable Long id, @PathVariable Long bicicletaId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.destrancar(id, bicicletaId));
    }
    
    @PostMapping("/{id}/status/{acao}")
    public ResponseEntity<TrancaDTO> postStatus(@PathVariable Long id, @PathVariable TrancaStatus acao) {
        return ResponseEntity.status(HttpStatus.OK).body(service.alterarStatus(id, acao));
    }

    @PostMapping("/integrarNaRede")
    public ResponseEntity<Void> postIntegrarRede(@RequestBody @Valid TrancaIntegracaoDTO request){
        service.integrarRede(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/RetirarDaRede")
    public ResponseEntity<Void> postRetirarRede(@RequestBody @Valid TrancaIntegracaoDTO request){
        service.retirarRede(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrancaDTO> putAtualizarTranca(@PathVariable Long id, @RequestBody @Valid TrancaRequestDTO novaTranca) {
        return ResponseEntity.status(HttpStatus.OK).body(service.atualizarTranca(id, novaTranca));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delTranca(@PathVariable Long id){
        service.deletar(id);
        return ResponseEntity.ok().build();
    }
}
