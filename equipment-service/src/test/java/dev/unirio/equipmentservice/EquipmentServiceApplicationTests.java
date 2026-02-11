package dev.unirio.equipmentservice;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class EquipmentServiceApplicationTests {

	@Test
	void contextLoads() {
		// Verifica se o contexto da aplicação é carregado corretamente pelo Spring
	}

	@Test
    void main() {
		assertTrue(true);
        EquipmentServiceApplication.main(new String[] {});
    }

}
