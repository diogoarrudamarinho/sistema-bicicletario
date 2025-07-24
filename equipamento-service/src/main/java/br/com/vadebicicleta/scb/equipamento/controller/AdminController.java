package br.com.vadebicicleta.scb.equipamento.controller;

import br.com.vadebicicleta.scb.equipamento.service.BicicletaService;
import br.com.vadebicicleta.scb.equipamento.service.TotemService;
import br.com.vadebicicleta.scb.equipamento.service.TrancaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AdminController {

    private final BicicletaService bicicletaService;
    private final TrancaService trancaService;
    private final TotemService totemService;

    public AdminController(BicicletaService bicicletaService, TrancaService trancaService, TotemService totemService) {
        this.bicicletaService = bicicletaService;
        this.trancaService = trancaService;
        this.totemService = totemService;
    }

    @GetMapping("/restaurarBanco")
    public ResponseEntity<Void> restaurarBanco() {
        trancaService.restaurarBanco();
        bicicletaService.restaurarBanco();
        totemService.restaurarBanco();

        return ResponseEntity.ok().build();
    }
}
