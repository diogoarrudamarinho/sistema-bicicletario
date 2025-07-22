package unirio.pm.external_service.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import unirio.pm.external_service.dto.EmailDTO;
import unirio.pm.external_service.entity.Email;

class EmailMapperTest {

    private EmailMapper mapper;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        mapper = new EmailMapper();
    }

    @Test
    @DisplayName("Deve converter de Email para EmailDTO corretamente")
    void testToDTO() {
        Email entity = new Email(
            "destinatario@email.com",
            "Assunto", 
            "Mensagem do e-mail");
            
        entity.setId(1L);

        EmailDTO dto = mapper.toDTO(entity);

        assertNotNull(dto);
        assertEquals(entity.getDestinatario(), dto.getDestinatario());
        assertEquals(entity.getAssunto(), dto.getAssunto());
        assertEquals(entity.getMensagem(), dto.getMensagem());
    }

    @Test
    @DisplayName("Deve converter de EmailDTO para Email corretamente")
    void testToEntity() {
        EmailDTO dto = new EmailDTO();
        dto.setDestinatario("destinatario@email.com");
        dto.setAssunto("Assunto");
        dto.setMensagem("Mensagem do e-mail");

        Email entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getDestinatario(), entity.getDestinatario());
        assertEquals(dto.getAssunto(), entity.getAssunto());
        assertEquals(dto.getMensagem(), entity.getMensagem());
    }

    @Test
    @DisplayName("Deve retornar null ao converter DTO ou Entidade nula")
    void testNullConversion() {
        assertNull(mapper.toDTO(null));
        assertNull(mapper.toEntity(null));
    }
}
