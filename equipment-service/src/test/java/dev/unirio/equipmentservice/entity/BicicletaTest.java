package dev.unirio.equipmentservice.entity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.unirio.equipmentservice.enumeration.BicicletaStatus;

@ExtendWith(MockitoExtension.class)
class BicicletaTest {
    
    private static final String MARCA = "Caloi";
    private static final String MODELO = "Elite";
    private static final String ANO = "2024";
    private static final Integer NUMERO = 101;
    private static final BicicletaStatus STATUS = BicicletaStatus.DISPONIVEL;

    @Test
    @DisplayName("Deve instanciar e permitir setters")
    void testConstructorVazioAndSetters() {
        Bicicleta bicicleta = assertDoesNotThrow((ThrowingSupplier<Bicicleta>) Bicicleta::new);
        assertNotNull(bicicleta);

        bicicleta.setMarca(MARCA);
        bicicleta.setModelo(MODELO);
        bicicleta.setAno(ANO);
        bicicleta.setNumero(NUMERO);
        bicicleta.setStatus(STATUS);

        assertEquals(MARCA, bicicleta.getMarca());
        assertEquals(MODELO, bicicleta.getModelo());
        assertEquals(ANO, bicicleta.getAno());
        assertEquals(NUMERO, bicicleta.getNumero());
        assertEquals(STATUS, bicicleta.getStatus());
    }

    @Test
    @DisplayName("Deve inicializar todos os campos")
    void testConstructorCompleto() {
        Bicicleta bicicleta = new Bicicleta(MARCA, MODELO, ANO, NUMERO, STATUS);

        assertEquals(MARCA, bicicleta.getMarca());
        assertEquals(MODELO, bicicleta.getModelo());
        assertEquals(NUMERO, bicicleta.getNumero());
        assertEquals(STATUS, bicicleta.getStatus());
    }
    
    @Test
    @DisplayName("Objetos com IDs Iguais devem ser considerados iguais")
    void testEqualsAndHashCode() {
        Bicicleta b1 = new Bicicleta(MARCA, MODELO, ANO, NUMERO, STATUS);
        b1.setId(1L);

        Bicicleta b2 = new Bicicleta("Outra Marca", "Outro Modelo", "2023", 999, BicicletaStatus.EM_USO);
        b2.setId(1L); 
        Bicicleta b3 = new Bicicleta();
        b3.setId(2L); 

        // Assert Equals
        assertEquals(b1, b2);
        assertNotEquals(b1, b3);
        
        // Cobertura de branches do equals
        assertEquals(b1, b1);
        assertNotEquals(null, b1);
        assertNotEquals(b1, new Object());
        
        // Assert HashCode
        assertEquals(b1.hashCode(), b2.hashCode());
        assertNotEquals(b1.hashCode(), b3.hashCode());
    }
    
    @Test
    @DisplayName("Objetos sem IDs (null) não devem ser iguais, exceto se for a mesma instância")
    void testEqualsAndHashCodeSemId() {
        Bicicleta b1 = new Bicicleta(MARCA, MODELO, ANO, NUMERO, STATUS);
        Bicicleta b2 = new Bicicleta(MARCA, MODELO, ANO, NUMERO, STATUS);

        assertEquals(b1, b2);
        assertEquals(b1, b1);
        
        b1.setId(null);
        b2.setId(1L);
        assertNotEquals(b1, b2);
    }
}
