package dev.unirio.equipmentservice.controller;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import dev.unirio.equipmentservice.dto.BicicletaDTO;
import dev.unirio.equipmentservice.dto.TrancaDTO;
import dev.unirio.equipmentservice.dto.TrancaIntegracaoDTO;
import dev.unirio.equipmentservice.dto.TrancaRequestDTO;
import dev.unirio.equipmentservice.enumeration.TrancaStatus;
import dev.unirio.equipmentservice.service.TrancaService;

@ExtendWith(MockitoExtension.class)
class TrancaControllerTest {

    @InjectMocks
    private TrancaController controller;

    @Mock
    private TrancaService service;

    @Test
    void testGetTrancas() {
        when(service.buscarTrancas()).thenReturn(List.of(new TrancaDTO()));

        ResponseEntity<List<TrancaDTO>> response = controller.getTrancas();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service).buscarTrancas();
    }

    @Test
    void testGetTranca() {
        when(service.buscarTranca(anyLong())).thenReturn(new TrancaDTO());

        ResponseEntity<TrancaDTO> response = controller.getTranca(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service).buscarTranca(1L);
    }

    @Test
    void testGetBicicletaDaTranca() {
        when(service.buscarBicicleta(anyLong())).thenReturn(new BicicletaDTO());

        ResponseEntity<BicicletaDTO> response = controller.getBicicleta(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service).buscarBicicleta(1L);
    }

    @Test
    void testPostTranca() {
        when(service.cadastrarTranca(any(TrancaRequestDTO.class)))
                .thenReturn(new TrancaDTO());

        ResponseEntity<TrancaDTO> response =
                controller.postTranca(new TrancaRequestDTO());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(service).cadastrarTranca(any(TrancaRequestDTO.class));
    }

    @Test
    void testPostTrancar() {
        when(service.trancar(anyLong(), anyLong()))
                .thenReturn(new TrancaDTO());

        ResponseEntity<TrancaDTO> response =
                controller.postTrancar(1L, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service).trancar(1L, 2L);
    }

    @Test
    void testPostDestrancar() {
        when(service.destrancar(anyLong(), anyLong()))
                .thenReturn(new TrancaDTO());

        ResponseEntity<TrancaDTO> response =
                controller.postDestrancar(1L, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service).destrancar(1L, 2L);
    }

    @Test
    void testPostStatus() {
        when(service.alterarStatus(anyLong(), any(TrancaStatus.class)))
                .thenReturn(new TrancaDTO());

        ResponseEntity<TrancaDTO> response =
                controller.postStatus(1L, TrancaStatus.LIVRE);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service).alterarStatus(1L, TrancaStatus.LIVRE);
    }

    @Test
    void testPostIntegrarRede() {
        doNothing().when(service).integrarRede(any(TrancaIntegracaoDTO.class));

        ResponseEntity<Void> response =
                controller.postIntegrarRede(new TrancaIntegracaoDTO());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service).integrarRede(any(TrancaIntegracaoDTO.class));
    }

    @Test
    void testPostRetirarRede() {
        doNothing().when(service).retirarRede(any(TrancaIntegracaoDTO.class));

        ResponseEntity<Void> response =
                controller.postRetirarRede(new TrancaIntegracaoDTO());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service).retirarRede(any(TrancaIntegracaoDTO.class));
    }

    @Test
    void testPutAtualizarTranca() {
        when(service.atualizarTranca(anyLong(), any(TrancaRequestDTO.class)))
                .thenReturn(new TrancaDTO());

        ResponseEntity<TrancaDTO> response =
                controller.putAtualizarTranca(1L, new TrancaRequestDTO());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service).atualizarTranca(anyLong(), any(TrancaRequestDTO.class));
    }

    @Test
    void testDeleteTranca() {
        doNothing().when(service).deletar(anyLong());

        ResponseEntity<Void> response = controller.delTranca(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service).deletar(1L);
    }
}
