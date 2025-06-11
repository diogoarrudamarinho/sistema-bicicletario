package unirio.pm.external_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import unirio.pm.external_service.dto.EmailDTO;
import unirio.pm.external_service.services.Services;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/externo")
public class Controller {
    
    @Autowired
    private Services services;

    @GetMapping("/hello")
    public String getHello(){
        return services.helloWorld();
    }

    @PostMapping("/mail")
    public ResponseEntity<EmailDTO> postEmail(@RequestBody EmailDTO email) {
        return ResponseEntity.ok(services.enviarEmail(email));
    }
}
