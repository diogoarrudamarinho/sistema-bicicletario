package br.com.vadebicicleta.scb.equipamento.controller;

import br.com.vadebicicleta.scb.equipamento.dto.AlteraTotemDTO;
import br.com.vadebicicleta.scb.equipamento.dto.BicicletaDTO;
import br.com.vadebicicleta.scb.equipamento.dto.NovoTotemDTO;
import br.com.vadebicicleta.scb.equipamento.dto.TotemDTO;
import br.com.vadebicicleta.scb.equipamento.dto.TrancaDTO;
import br.com.vadebicicleta.scb.equipamento.service.TotemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/totem")
public class TotemController {

    private final TotemService totemService;

    public TotemController(TotemService totemService) {
        this.totemService = totemService;
    }

    @PostMapping
    public ResponseEntity<TotemDTO> cadastrarTotem(@Valid @RequestBody NovoTotemDTO dto) {
        TotemDTO totemSalvo = totemService.cadastrarTotem(dto);
        return new ResponseEntity<>(totemSalvo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TotemDTO>> listarTotens() {
        return ResponseEntity.ok(totemService.listarTodos());
    }

    @GetMapping("/{idTotem}")
    public ResponseEntity<TotemDTO> buscarPorId(@PathVariable("idTotem") Long idTotem) {
        return ResponseEntity.ok(totemService.buscarPorId(idTotem));
    }

    @PutMapping("/{idTotem}")
    public ResponseEntity<TotemDTO> alterarTotem(@PathVariable("idTotem") Long idTotem, @Valid @RequestBody AlteraTotemDTO dto) {
        return ResponseEntity.ok(totemService.alterarTotem(idTotem, dto));
    }

    @GetMapping("/{idTotem}/trancas")
    public ResponseEntity<List<TrancaDTO>> listarTrancasDoTotem(@PathVariable("idTotem") Long idTotem) {
        List<TrancaDTO> trancas = totemService.listarTrancasDoTotem(idTotem);
        return ResponseEntity.ok(trancas);
    }

    @GetMapping("/{idTotem}/bicicletas")
    public ResponseEntity<List<BicicletaDTO>> listarBicicletasDoTotem(@PathVariable("idTotem") Long idTotem) {
        List<BicicletaDTO> bicicletas = totemService.listarBicicletasDoTotem(idTotem);
        return ResponseEntity.ok(bicicletas);
    }

    @DeleteMapping("/{idTotem}")
    public ResponseEntity<Void> deletar(@PathVariable("idTotem") Long idTotem) {
        totemService.deletar(idTotem);
        return ResponseEntity.noContent().build();
    }
}
