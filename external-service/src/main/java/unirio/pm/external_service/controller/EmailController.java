package unirio.pm.external_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import unirio.pm.external_service.dto.EmailDTO;
import unirio.pm.external_service.services.EmailService;

@RestController
@RequestMapping("/email")
public class EmailController {
    
    private final EmailService service;

    public EmailController(EmailService service) {
        this.service = service;
    }

    @GetMapping("/hello")
    public String getHello(){
        return service.helloWorld();
    }

    @PostMapping("/enviarEmail")
    public ResponseEntity<EmailDTO> postEmail(@RequestBody @Valid EmailDTO email) {
        return ResponseEntity.ok(service.enviarEmail(email));
    }
}
