package unirio.pm.external_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import unirio.pm.external_service.dto.CobrancaDTO;
import unirio.pm.external_service.dto.CobrancaRequestDTO;
import unirio.pm.external_service.services.CobrancaService;

@RestController
@RequestMapping("/cobranca")
public class CobrancaController {
    
    @Autowired
    private CobrancaService service;

    @PostMapping()
    public ResponseEntity<CobrancaDTO> cobrar(@RequestBody CobrancaRequestDTO cobranca) {        
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarCobranca(cobranca));
    }
}
