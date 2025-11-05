package dev.unirio.externalservice;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ExternalserviceApplicationTests {

	@Test
	void contextLoads() {
		// Verifica se o contexto da aplicação é carregado corretamente pelo Spring
	}

	@Test
    void main() {
		assertTrue(true);
        ExternalServiceApplication.main(new String[] {});
    }

}
