package br.com.vadebicicleta.scb.equipamento.controller;

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

import br.com.vadebicicleta.scb.equipamento.dto.AlteraBicicletaDTO;
import br.com.vadebicicleta.scb.equipamento.dto.BicicletaDTO;
import br.com.vadebicicleta.scb.equipamento.dto.IntegrarNaRedeDTO;
import br.com.vadebicicleta.scb.equipamento.dto.NovaBicicletaDTO;
import br.com.vadebicicleta.scb.equipamento.dto.RetirarDaRedeDTO;
import br.com.vadebicicleta.scb.equipamento.service.BicicletaService;
import jakarta.validation.Valid;

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
    public ResponseEntity<BicicletaDTO> getBicicletaById(@PathVariable("idBicicleta") Long idBicicleta) {
        BicicletaDTO bicicletaDTO = bicicletaService.buscarPorId(idBicicleta);
        return ResponseEntity.ok(bicicletaDTO);
    }

    @PutMapping("/{idBicicleta}")
    public ResponseEntity<BicicletaDTO> alterarBicicleta(@PathVariable("idBicicleta") Long idBicicleta, @Valid @RequestBody AlteraBicicletaDTO alteraBicicletaDTO) {
        BicicletaDTO bicicletaAtualizada = bicicletaService.alterarBicicleta(idBicicleta, alteraBicicletaDTO);
        return ResponseEntity.ok(bicicletaAtualizada);
    }

    @DeleteMapping("/{idBicicleta}")
    public ResponseEntity<Void> deletarBicicleta(@PathVariable("idBicicleta") Long idBicicleta) {
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
    public ResponseEntity<BicicletaDTO> alterarStatus(@PathVariable("idBicicleta") Long idBicicleta, @PathVariable String acao) {
        BicicletaDTO bicicletaAtualizada = bicicletaService.alterarStatus(idBicicleta, acao);
        return ResponseEntity.ok(bicicletaAtualizada);
    }
}
