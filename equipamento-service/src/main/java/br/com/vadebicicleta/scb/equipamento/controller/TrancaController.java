package br.com.vadebicicleta.scb.equipamento.controller;

import br.com.vadebicicleta.scb.equipamento.dto.*;
import br.com.vadebicicleta.scb.equipamento.service.TrancaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tranca")
public class TrancaController {

    private final TrancaService trancaService;

    public TrancaController(TrancaService trancaService) {
        this.trancaService = trancaService;
    }

    @GetMapping
    public ResponseEntity<List<TrancaDTO>> listarTrancas() {
        return ResponseEntity.ok(trancaService.listarTodas());
    }

    @PostMapping
    public ResponseEntity<TrancaDTO> cadastrarTranca(@Valid @RequestBody NovaTrancaDTO dto) {
        TrancaDTO trancaSalva = trancaService.cadastrarTranca(dto);
        return new ResponseEntity<>(trancaSalva, HttpStatus.CREATED);
    }

    @GetMapping("/{idTranca}")
    public ResponseEntity<TrancaDTO> buscarPorId(@PathVariable Long idTranca) {
        return ResponseEntity.ok(trancaService.buscarPorId(idTranca));
    }

    @PutMapping("/{idTranca}")
    public ResponseEntity<TrancaDTO> alterarTranca(@PathVariable Long idTranca, @Valid @RequestBody AlteraTrancaDTO dto) {
        return ResponseEntity.ok(trancaService.alterarTranca(idTranca, dto));
    }

    @DeleteMapping("/{idTranca}")
    public ResponseEntity<Void> deletar(@PathVariable Long idTranca) {
        trancaService.deletar(idTranca);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{idTranca}/integrarNaRede")
    public ResponseEntity<Void> integrarNaRede(@PathVariable Long idTranca, @Valid @RequestBody TrancaIntegrarDTO dtoBody) {
        trancaService.integrarNaRede(idTranca, dtoBody);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{idTranca}/retirarDaRede")
    public ResponseEntity<Void> retirarDaRede(@PathVariable Long idTranca, @Valid @RequestBody TrancaRetirarDTO dtoBody) {
        trancaService.retirarDaRede(idTranca, dtoBody);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{idTranca}/bicicleta")
    public ResponseEntity<BicicletaDTO> obterBicicleta(@PathVariable Long idTranca) {
        return ResponseEntity.ok(trancaService.obterBicicleta(idTranca));
    }

    @PostMapping("/{idTranca}/status/{acao}")
    public ResponseEntity<TrancaDTO> alterarStatus(@PathVariable Long idTranca, @PathVariable String acao) {
        return ResponseEntity.ok(trancaService.alterarStatus(idTranca, acao));
    }

    @PostMapping("/{idTranca}/trancar")
    public ResponseEntity<TrancaDTO> trancar(@PathVariable Long idTranca, @RequestBody Long idBicicleta) {
        return ResponseEntity.ok(trancaService.trancar(idTranca, idBicicleta));
    }

    @PostMapping("/{idTranca}/destrancar")
    public ResponseEntity<TrancaDTO> destrancar(@PathVariable Long idTranca) {
        return ResponseEntity.ok(trancaService.destrancar(idTranca));
    }
}
