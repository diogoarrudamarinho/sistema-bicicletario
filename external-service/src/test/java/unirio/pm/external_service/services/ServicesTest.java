package unirio.pm.external_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServicesTest {
   
    @Autowired
    private Services services;
    
    @Test
    @DisplayName("Should return 'Hello world' when is called")
    void testHelloWorld() {
        String resultado = services.helloWorld();
        assertEquals("Hello world", resultado);
    }
}
