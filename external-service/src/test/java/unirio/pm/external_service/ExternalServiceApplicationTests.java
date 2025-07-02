package unirio.pm.external_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ExternalServiceApplicationTests {

	@Test
	public void contextLoads() {
		// Verifica se o contexto da aplicação é carregado corretamente pelo Spring
	}

	@Test
    public void main() {
        ExternalServiceApplication.main(new String[] {});
    }
}
