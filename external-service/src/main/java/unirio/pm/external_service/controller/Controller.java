package unirio.pm.external_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import unirio.pm.external_service.services.Services;

@RestController
@RequestMapping("/externo")
public class Controller {
    
    @Autowired
    private Services services;

    @GetMapping("/hello")
    public String getHello(){
        return services.helloWorld();
    }
}
