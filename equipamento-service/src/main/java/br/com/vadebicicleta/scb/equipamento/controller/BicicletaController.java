package br.com.vadebicicleta.scb.equipamento.controller;

import br.com.vadebicicleta.scb.equipamento.dto.AlteraBicicletaDTO;
import br.com.vadebicicleta.scb.equipamento.dto.BicicletaDTO;
import br.com.vadebicicleta.scb.equipamento.dto.IntegrarNaRedeDTO;
import br.com.vadebicicleta.scb.equipamento.dto.NovaBicicletaDTO;
import br.com.vadebicicleta.scb.equipamento.dto.RetirarDaRedeDTO;
import br.com.vadebicicleta.scb.equipamento.service.BicicletaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bicicleta")
public class BicicletaController {

    private final BicicletaService bicicletaService;

    public BicicletaController(BicicletaService bicicletaService) {
        this.bicicletaService = bicicletaService;
    }

    @PostMapping
    public ResponseEntity<BicicletaDTO> cadastrarBicicleta(@Valid @RequestBody NovaBicicletaDTO novaBicicletaDTO) {
        BicicletaDTO bicicletaSalva = bicicletaService.cadastrarBicicleta(novaBicicletaDTO);
        return new ResponseEntity<>(bicicletaSalva, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BicicletaDTO>> listarBicicletas() {
        List<BicicletaDTO> bicicletas = bicicletaService.listarTodas();
        return ResponseEntity.ok(bicicletas);
    }

    @GetMapping("/{idBicicleta}")
    public ResponseEntity<BicicletaDTO> getBicicletaById(@PathVariable("idBicicleta") UUID idBicicleta) {
        BicicletaDTO bicicletaDTO = bicicletaService.buscarPorId(idBicicleta);
        return ResponseEntity.ok(bicicletaDTO);
    }

    @PutMapping("/{idBicicleta}")
    public ResponseEntity<BicicletaDTO> alterarBicicleta(@PathVariable("idBicicleta") UUID idBicicleta, @Valid @RequestBody AlteraBicicletaDTO alteraBicicletaDTO) {
        BicicletaDTO bicicletaAtualizada = bicicletaService.alterarBicicleta(idBicicleta, alteraBicicletaDTO);
        return ResponseEntity.ok(bicicletaAtualizada);
    }

    @DeleteMapping("/{idBicicleta}")
    public ResponseEntity<Void> deletarBicicleta(@PathVariable("idBicicleta") UUID idBicicleta) {
        bicicletaService.deletar(idBicicleta);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/integrarNaRede")
    public ResponseEntity<Void> integrarNaRede(@Valid @RequestBody IntegrarNaRedeDTO dto) {
        bicicletaService.integrarNaRede(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/retirarDaRede")
    public ResponseEntity<Void> retirarDaRede(@Valid @RequestBody RetirarDaRedeDTO dto) {
        bicicletaService.retirarDaRede(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{idBicicleta}/status/{acao}")
    public ResponseEntity<BicicletaDTO> alterarStatus(@PathVariable("idBicicleta") UUID idBicicleta, @PathVariable String acao) {
        BicicletaDTO bicicletaAtualizada = bicicletaService.alterarStatus(idBicicleta, acao);
        return ResponseEntity.ok(bicicletaAtualizada);
    }
}
