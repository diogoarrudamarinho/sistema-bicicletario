package br.com.vadebicicleta.scb.equipamento;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class EquipamentoApplication {


//test
	@GetMapping("/")
	public String home() {
		return "Spring is here!";
	}

	public static void main(String[] args) {
		SpringApplication.run(EquipamentoApplication.class, args);
	}
}
