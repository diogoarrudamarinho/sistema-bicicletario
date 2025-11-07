package dev.unirio.externalservice.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ObjectNotFoundExceptionTest {
    
    @Test
    @DisplayName("Deve validar a mensagem e id")
    void testExceptionMessageAndId() {
        Long expectedId = 10L;
        String expectedMessage = "Not Found";

        ObjectNotFoundException ex = new ObjectNotFoundException(expectedMessage, expectedId);

        assertEquals(expectedMessage, ex.getMessage());
        assertEquals(expectedId, ex.getId());
    }
}
