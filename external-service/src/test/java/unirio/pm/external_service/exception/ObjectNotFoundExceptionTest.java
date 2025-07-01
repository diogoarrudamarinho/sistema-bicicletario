package unirio.pm.external_service.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ObjectNotFoundExceptionTest {
    
    @Test
    public void testExceptionMessageAndId() {
        Long expectedId = 10L;
        String expectedMessage = "Not Found";

        ObjectNotFoundException ex = new ObjectNotFoundException(expectedMessage, expectedId);

        assertEquals(expectedMessage, ex.getMessage());
        assertEquals(expectedId, ex.getId());
    }
}
