package dev.unirio.equipmentservice.controller;

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
import dev.unirio.equipmentservice.dto.BicicletaRequestDTO;
import dev.unirio.equipmentservice.service.BicicletaService;

@ExtendWith(MockitoExtension.class)
class BicicletaControllerTest {
    
    @InjectMocks
    private BicicletaController controller;

    @Mock
    private BicicletaService service;

    @Test
    void testGetBicileta(){
        Long id = 1L;
        BicicletaDTO bicicleta = new BicicletaDTO();
        
        when(service.buscarBicicleta(id)).thenReturn(bicicleta);

        ResponseEntity<BicicletaDTO> response = controller.getBicicleta(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service).buscarBicicleta(id);
    }

    @Test
    void testPostBicileta(){
        BicicletaRequestDTO novaBicicleta = new BicicletaRequestDTO();
        BicicletaDTO bicicleta = new BicicletaDTO();
        
        when(service.criarBicicleta(novaBicicleta)).thenReturn(bicicleta);

        ResponseEntity<BicicletaDTO> response = controller.postBicicleta(novaBicicleta);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(service).criarBicicleta(novaBicicleta);
    }

    @Test
    void testPutBicicleta(){

        when(service.atualizarBicicleta(
            anyLong(), 
            any(BicicletaRequestDTO.class)))
        .thenReturn(new BicicletaDTO());

        ResponseEntity<BicicletaDTO> response = controller.putBicicleta(1L, new BicicletaRequestDTO());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service).atualizarBicicleta(anyLong(), any(BicicletaRequestDTO.class));
    }

    @Test
    void testDelBicicleta(){

        doNothing().when(service).deletarBicicleta(anyLong());

        ResponseEntity<Void> response = controller.delBicicleta(anyLong());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service).deletarBicicleta(anyLong());
    }
}
