package dev.unirio.externalservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.unirio.externalservice.dto.EmailDTO;
import dev.unirio.externalservice.service.EmailService;
import jakarta.validation.Valid;

@RestController
public class EmailController {
    private final EmailService service;

    public EmailController(EmailService service) {
        this.service = service;
    }

    @PostMapping("/enviarEmail")
    public ResponseEntity<EmailDTO> postEmail(@RequestBody @Valid EmailDTO email) {
        return ResponseEntity.ok(service.enviarEmail(email));
    }
}
