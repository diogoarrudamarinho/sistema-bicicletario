package dev.unirio.rentalservice.mapper;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.unirio.rentalservice.dto.CiclistaDTO;
import dev.unirio.rentalservice.dto.CiclistaRequestDTO;
import dev.unirio.rentalservice.dto.PassaporteDTO;
import dev.unirio.rentalservice.entity.Ciclista;
import dev.unirio.rentalservice.enumeration.CiclistaStatus;
import dev.unirio.rentalservice.enumeration.Nacionalidade;

@ExtendWith(MockitoExtension.class)
class CiclistaMapperTest {
    @Mock
    private CartaoMapper cartaoMapper;

    @InjectMocks
    private CiclistaMapperImpl mapper; 

    private Ciclista ciclista;
    private CiclistaDTO ciclistaDTO;
    private CiclistaRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        ciclista = new Ciclista();
        ciclista.setId(1L);
        ciclista.setNome("Diogo");
        ciclista.setUrlFotoDocumento("12345");

        // Ajuste os valores conforme a estrutura exata do seu Record/Class DTO
        ciclistaDTO = new CiclistaDTO(1L, "Diogo", "email@test.com", "123", Nacionalidade.BRASILEIRO, 
                                     LocalDate.now(), null, CiclistaStatus.ATIVO, 12345L);

        requestDTO = new CiclistaRequestDTO("Novo Nome", "novo@email.com", "12345678901", "123", Nacionalidade.BRASILEIRO, LocalDate.now(), null, "123", null);
    }

    @Test
    void toDto() {
        CiclistaDTO result = mapper.toDto(ciclista);
        assertNotNull(result);
        assertEquals(12345L, result.urlFotoDocumento()); 
    }

    @Test
    void toDtoNull() {
        assertNull(mapper.toDto(null));
    }

    @Test
    void toEntity() {
        Ciclista result = mapper.toEntity(ciclistaDTO);
        assertNotNull(result);
        assertEquals("12345", result.getUrlFotoDocumento()); 
    }

    @Test
    void toEntityFromRequest() {
        Ciclista result = mapper.toEntityFromRequest(requestDTO);
        assertNotNull(result);
        verify(cartaoMapper).toEntity(any()); 
    }

    @Test
    void updateEntityFromDto() {
        ciclista.setCartao(null);
        ciclista.setPassaporte(null);

        mapper.updateEntityFromDto(requestDTO, ciclista);

        assertNull(ciclista.getCartao());
        assertNull(ciclista.getPassaporte());
        assertEquals("Novo Nome", ciclista.getNome());
    }

    @Test
    void updateEntityFromDtoNullFields() {
        CiclistaRequestDTO requestVazio = new CiclistaRequestDTO(null, null, null, null, null, null, null, null, null);
        
        mapper.updateEntityFromDto(requestVazio, ciclista);

        assertNull(ciclista.getCartao());
        assertNull(ciclista.getPassaporte());
        assertNull(ciclista.getUrlFotoDocumento());
    }

    @Test
    void testPassaporteToPassaporteDTO_Null() {
        assertNull(mapper.passaporteToPassaporteDTO(null));
    }

    @Test
    void testPassaporteDTOToPassaporte_Null() {
        assertNull(mapper.passaporteDTOToPassaporte(null));
    }
}
