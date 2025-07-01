package unirio.pm.external_service.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import unirio.pm.external_service.dto.CobrancaDTO;
import unirio.pm.external_service.dto.CobrancaRequestDTO;
import unirio.pm.external_service.enumerations.StatusCobranca;
import unirio.pm.external_service.services.CobrancaService;

@ExtendWith(MockitoExtension.class)
public class CobrancaControllerTest {
    
    @InjectMocks
    private CobrancaController controller;

    @Mock
    private CobrancaService service;

    @Test
    @DisplayName("Deve criar cobrança e retornar 201 CREATED")
    public void testCriarCobranca() {

        CobrancaRequestDTO request = new CobrancaRequestDTO();
        CobrancaDTO cobranca = new CobrancaDTO();

        when(service.criarCobranca(any(CobrancaRequestDTO.class))).thenReturn(cobranca);

        ResponseEntity<CobrancaDTO> response = controller.criarCobranca(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(service).criarCobranca(any(CobrancaRequestDTO.class));
    }

    @Test
    @DisplayName("Deve processar cobrancas em fila e retornar 200 OK")
    public void testProcessaCobranca() {

        CobrancaDTO cobranca = new CobrancaDTO(
            1L,
            new BigDecimal("100.00"),
            42L,
            StatusCobranca.PAGA,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        when(service.processarFilaCobranca()).thenReturn(List.of(cobranca));

        ResponseEntity<List<CobrancaDTO>> response = controller.processarFila();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(service).processarFilaCobranca();
    }
}
