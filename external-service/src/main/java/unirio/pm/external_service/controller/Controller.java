package unirio.pm.external_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/externo")
public class Controller {
    
    @GetMapping("/hello")
    public String getHello(){
        return "Hello world";
    }
}
