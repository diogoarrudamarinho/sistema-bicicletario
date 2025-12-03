package dev.unirio.equipmentservice.entity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

import dev.unirio.equipmentservice.enumeration.TrancaStatus;

class TrancaTest {

    private static final Integer NUMERO = 5;
    private static final String LOCALIZACAO = "Rua A";
    private static final String ANO_FAB = "2020";
    private static final String MODELO = "Tranca boa Uau";
    private static final TrancaStatus STATUS = TrancaStatus.LIVRE;
    private static final Bicicleta MOCK_BICICLETA = new Bicicleta();

    @Test
    @DisplayName("Deve instanciar e permitir setters")
    void testConstructorVazioAndSetters() {
        Tranca tranca = assertDoesNotThrow((ThrowingSupplier<Tranca>)Tranca::new);
        assertNotNull(tranca);

        tranca.setNumero(NUMERO);
        tranca.setLocalizacao(LOCALIZACAO);
        tranca.setAnoDeFabricacao(ANO_FAB);
        tranca.setModelo(MODELO);
        tranca.setStatus(STATUS);
        tranca.setBicicleta(MOCK_BICICLETA);

        assertEquals(NUMERO, tranca.getNumero());
        assertEquals(LOCALIZACAO, tranca.getLocalizacao());
        assertEquals(MOCK_BICICLETA, tranca.getBicicleta());
        assertEquals(ANO_FAB, tranca.getAnoDeFabricacao());
        assertEquals(MODELO, tranca.getModelo());
        assertNull(tranca.getId());
    }

    @Test
    @DisplayName("Deve inicializar todos os campos principais")
    void testConstructorCompleto() {
        Tranca tranca = new Tranca(NUMERO, LOCALIZACAO, ANO_FAB, MODELO, STATUS);

        assertEquals(NUMERO, tranca.getNumero());
        assertEquals(LOCALIZACAO, tranca.getLocalizacao());
        assertEquals(STATUS, tranca.getStatus());
        assertNull(tranca.getBicicleta(), "Bicicleta deve ser null por padrão.");
    }
    
    @Test
    @DisplayName("Trancas com IDs Iguais devem ser consideradas iguais")
    void testEqualsAndHashCode_ComId() {
        Tranca t1 = new Tranca(NUMERO, LOCALIZACAO, ANO_FAB, MODELO, STATUS);
        t1.setId(100L);
        t1.setBicicleta(MOCK_BICICLETA);

        Tranca t2 = new Tranca(999, "Outro", "2021", "Modelo B", TrancaStatus.OCUPADA);
        t2.setId(100L); // Mesmo ID

        Tranca t3 = new Tranca();
        t3.setId(200L); // ID Diferente

        Tranca t4 = new Tranca();
        t4.setId(null);

        // Assert Equals
        assertEquals(t1, t2);
        assertEquals(t1, t1);
        assertNotEquals(t1, t3);
        assertNotEquals(t4, t1);
        
        // Assert HashCode
        assertEquals(t1.hashCode(), t2.hashCode());
        assertNotEquals(t1.hashCode(), t3.hashCode());
    }
}
