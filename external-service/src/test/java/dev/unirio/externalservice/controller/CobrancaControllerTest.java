package dev.unirio.externalservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import dev.unirio.externalservice.dto.CobrancaDTO;
import dev.unirio.externalservice.dto.CobrancaRequestDTO;
import dev.unirio.externalservice.enumeration.StatusCobranca;
import dev.unirio.externalservice.service.CobrancaService;

@ExtendWith(MockitoExtension.class)
class CobrancaControllerTest {
    
    @InjectMocks
    private CobrancaController controller;

    @Mock
    private CobrancaService service;

    @Test
    @DisplayName("Deve criar cobrança e retornar 201 CREATED")
    void testCriarCobranca() {

        CobrancaRequestDTO request = new CobrancaRequestDTO();
        CobrancaDTO cobranca = new CobrancaDTO();

        when(service.criarCobranca(any(CobrancaRequestDTO.class))).thenReturn(cobranca);

        ResponseEntity<CobrancaDTO> response = controller.criarCobranca(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(service).criarCobranca(any(CobrancaRequestDTO.class));
    }

    @Test
    @DisplayName("Deve processar cobrancas em fila e retornar 200 OK")
    void testProcessaCobranca() {

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

    @Test
    @DisplayName("Deve buscar e retornar uma Cobranca")
    void testBuscarCobranca(){
        CobrancaDTO cobranca = new CobrancaDTO(
            1L,
            new BigDecimal("100.00"),
            42L,
            StatusCobranca.PAGA,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        when(service.buscarCobranca(1L)).thenReturn(cobranca);

        ResponseEntity<CobrancaDTO> response = controller.buscarCobranca(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(service).buscarCobranca(1L);
        
    }
}
