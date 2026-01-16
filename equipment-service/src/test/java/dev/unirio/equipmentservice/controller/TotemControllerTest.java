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
import dev.unirio.equipmentservice.dto.TotemDTO;
import dev.unirio.equipmentservice.dto.TotemRequestDTO;
import dev.unirio.equipmentservice.dto.TrancaDTO;
import dev.unirio.equipmentservice.service.TotemService;
import dev.unirio.equipmentservice.service.TrancaService;

@ExtendWith(MockitoExtension.class)
class TotemControllerTest {

    @InjectMocks
    private TotemController controller;

    @Mock
    private TotemService service;

    @Mock
    private TrancaService trancaService;

    @Test
    void testGetTotem() {
        when(service.buscarTotem(anyLong())).thenReturn(new TotemDTO());

        ResponseEntity<TotemDTO> response = controller.getTotem(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service).buscarTotem(1L);
    }

    @Test
    void testGetTotens() {
        when(service.buscarTotens()).thenReturn(List.of(new TotemDTO()));

        ResponseEntity<List<TotemDTO>> response = controller.getTotens();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service).buscarTotens();
    }

    @Test
    void testGetTrancasPorTotem() {
        when(trancaService.buscarTrancasPorTotem(anyLong()))
                .thenReturn(List.of(new TrancaDTO()));

        ResponseEntity<List<TrancaDTO>> response = controller.getTrancas(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(trancaService).buscarTrancasPorTotem(1L);
    }

    @Test
    void testGetBicicletasPorTotem() {
        when(trancaService.buscarBicicletasPorTotem(anyLong()))
                .thenReturn(List.of(new BicicletaDTO()));

        ResponseEntity<List<BicicletaDTO>> response = controller.getBicicletas(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(trancaService).buscarBicicletasPorTotem(1L);
    }

    @Test
    void testPostTotem() {
        TotemRequestDTO request = new TotemRequestDTO();
        when(service.criarTotem(any(TotemRequestDTO.class))).thenReturn(new TotemDTO());

        ResponseEntity<TotemDTO> response = controller.postTotem(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(service).criarTotem(request);
    }

    @Test
    void testPutTotem() {
        when(service.atualizarTotem(anyLong(), any(TotemRequestDTO.class)))
                .thenReturn(new TotemDTO());

        ResponseEntity<TotemDTO> response =
                controller.putTotem(1L, new TotemRequestDTO());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service).atualizarTotem(anyLong(), any(TotemRequestDTO.class));
    }

    @Test
    void testDeleteTotem() {
        doNothing().when(service).deletarTotem(anyLong());

        ResponseEntity<Void> response = controller.deleteTotem(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service).deletarTotem(1L);
    }
}
