package dev.unirio.equipmentservice.entity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

class TotemTest {
    
    private static final String LOCALIZACAO = "Praça Central - RJ";
    private static final String DESCRICAO = "Ao lado do quiosque";


    @Test
    @DisplayName("Construtor Vazio: Deve instanciar e permitir setters")
    void testConstructorVazioAndSetters() {
        Totem totem = assertDoesNotThrow((ThrowingSupplier<Totem>) Totem::new);
        assertNotNull(totem);

        totem.setLocalizacao(LOCALIZACAO);
        totem.setDescricao(DESCRICAO);

        assertEquals(LOCALIZACAO, totem.getLocalizacao());
        assertEquals(DESCRICAO, totem.getDescricao());
        assertNull(totem.getId());
    }

    @Test
    @DisplayName("Deve inicializar localizacao e descricao")
    void testConstructorCompleto() {
        Totem totem = new Totem(LOCALIZACAO, DESCRICAO);

        assertEquals(LOCALIZACAO, totem.getLocalizacao());
        assertEquals(DESCRICAO, totem.getDescricao());
    }


    @Test
    @DisplayName("Objetos com IDs Iguais devem ser considerados iguais")
    void testEqualsAndHashCode() {
        Totem t1 = new Totem(LOCALIZACAO, DESCRICAO);
        t1.setId(10L);

        Totem t2 = new Totem("Outra", "Outra");
        t2.setId(10L); // Mesmo ID

        Totem t3 = new Totem();
        t3.setId(20L); // ID Diferente

        // Assert Equals
        assertEquals(t1, t2);
        assertEquals(t1, t1);
        assertNotEquals(t1, t3);
        assertNotEquals(null, t1);
        assertNotEquals(t1, new Object());
        
        // Assert HashCode
        assertEquals(t1.hashCode(), t2.hashCode());
        assertNotEquals(t1.hashCode(), t3.hashCode());
    }
}
