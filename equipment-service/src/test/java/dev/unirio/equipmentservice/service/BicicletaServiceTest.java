package dev.unirio.equipmentservice.service;

import java.util.Optional;

import org.hibernate.ObjectNotFoundException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.unirio.equipmentservice.dto.BicicletaDTO;
import dev.unirio.equipmentservice.dto.BicicletaRequestDTO;
import dev.unirio.equipmentservice.entity.Bicicleta;
import dev.unirio.equipmentservice.enumeration.BicicletaStatus;
import dev.unirio.equipmentservice.exception.NegocioException;
import dev.unirio.equipmentservice.mapper.BicicletaMapper;
import dev.unirio.equipmentservice.repository.BicicletaRepository;
import dev.unirio.equipmentservice.service.implementation.BicicletaServiceImplementation;

@ExtendWith(MockitoExtension.class)
class BicicletaServiceTest {

    @Mock
    private BicicletaRepository repository; 
    
    @Mock
    private BicicletaMapper mapper;         

    @InjectMocks
    private BicicletaServiceImplementation service;

    private static final Long ID = 1L;
    private static final Long ID_INVALIDO = 99L;
    private Bicicleta bicicleta;
    private BicicletaDTO bicicletaDTO;
    private BicicletaRequestDTO requestDTO;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        bicicleta = new Bicicleta(); 
        bicicleta.setId(ID);
        
        bicicletaDTO = new BicicletaDTO();
        bicicletaDTO.setId(ID);
        
        requestDTO = new BicicletaRequestDTO("Caloi", "Elite", "2024", 101, null);
    }

    /* BUSCAR BICICLETA */

    @SuppressWarnings("null")
    @Test
    void buscarBicicleta() {
        when(repository.findById(ID)).thenReturn(Optional.of(bicicleta));
        when(mapper.toDto(bicicleta)).thenReturn(bicicletaDTO);

        BicicletaDTO resultado = service.buscarBicicleta(ID);

        assertNotNull(resultado);
        assertEquals(ID, resultado.getId());
        verify(repository, times(1)).findById(ID); 
        verify(mapper, times(1)).toDto(bicicleta);
    }

    @SuppressWarnings("null")
    @Test
    void buscarBicicletaException() {
        when(repository.findById(ID_INVALIDO)).thenReturn(Optional.empty());

        ObjectNotFoundException thrown = assertThrows(ObjectNotFoundException.class, () -> {
            service.buscarBicicleta(ID_INVALIDO);
        });

        assertTrue(thrown.getMessage().contains("Bicicleta não encontrada"));
        verify(repository, times(1)).findById(ID_INVALIDO);
        verify(mapper, never()).toDto(any());
    }
    
    @SuppressWarnings("null")
    @Test
    void buscarBicicletaNull() {
        Throwable exception = assertThrows(NegocioException.class, () -> service.buscarBicicleta(null));
        assertNotNull(exception); 
    }

    /* BUSCAR ENTIDADE */

    @Test
    void buscarEntidade() {
        when(repository.findById(ID)).thenReturn(Optional.of(bicicleta));

        Bicicleta resultado = service.buscarEntidade(ID);

        assertNotNull(resultado);
        assertEquals(ID, resultado.getId());
        verify(repository, times(1)).findById(ID); 
    }

    @SuppressWarnings("null")
    @Test
    void buscarEntidadeException() {
        when(repository.findById(ID_INVALIDO)).thenReturn(Optional.empty());

        ObjectNotFoundException thrown = assertThrows(ObjectNotFoundException.class, () -> {
            service.buscarEntidade(ID_INVALIDO);
        });

        assertTrue(thrown.getMessage().contains("Bicicleta não encontrada"));
        verify(repository, times(1)).findById(ID_INVALIDO);
        verify(mapper, never()).toDto(any());
    }
    
    @SuppressWarnings("null")
    @Test
    void buscarEntidadeNull() {
        Throwable exception = assertThrows(NegocioException.class, () -> service.buscarEntidade(null));
        assertNotNull(exception); 
    }

    /* CRIAR BICICLETA */
    @SuppressWarnings("null")
    @Test
    void criarBicicleta() {
        when(mapper.toEntity(requestDTO)).thenReturn(bicicleta); 
        when(repository.save(bicicleta)).thenReturn(bicicleta); 
        when(mapper.toDto(bicicleta)).thenReturn(bicicletaDTO); 

        BicicletaDTO resultado = service.criarBicicleta(requestDTO);

        assertNotNull(resultado);
        assertEquals(ID, resultado.getId());
        verify(repository, times(1)).save(bicicleta);
        verify(mapper, times(1)).toEntity(requestDTO);
    }
    
    @SuppressWarnings("null")
    @Test
    void criarBicicletaMapperNull() {
        when(mapper.toEntity(requestDTO)).thenReturn(null); 

        Throwable exception = assertThrows(NegocioException.class, () -> service.criarBicicleta(requestDTO));
        assertNotNull(exception); 
    }

    /* ALTERAR STATUS */
    
    @Test
    void testAlterarStatus(){
        when(repository.findById(ID)).thenReturn(Optional.of(bicicleta));
        when(repository.save(bicicleta)).thenReturn(bicicleta);
        when(mapper.toDto(bicicleta)).thenReturn(bicicletaDTO);

        BicicletaDTO request = service.alterarStatus(ID, BicicletaStatus.NOVA);

        assertEquals(BicicletaStatus.NOVA, bicicleta.getStatus());
        assertNotNull(request);
    }

    @Test
    void testAlterarStatusNotFound(){
        when(repository.findById(ID_INVALIDO)).thenReturn(Optional.empty());

        Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.alterarStatus(ID_INVALIDO, BicicletaStatus.APOSENTADA));
        assertNotNull(exception); 
    }
    
    @Test
    void testAlterarStatusIdNull(){
        Throwable exception = assertThrows(NegocioException.class, () -> service.alterarStatus(null, BicicletaStatus.DISPONIVEL));
        assertNotNull(exception); 
    }

    /* ATUALIZAR BICICLETA */

    @SuppressWarnings("null")
    @Test
    void atualizarBicicleta() {
        when(repository.findById(ID)).thenReturn(Optional.of(bicicleta)); 
        when(repository.save(bicicleta)).thenReturn(bicicleta);
        when(mapper.toDto(bicicleta)).thenReturn(bicicletaDTO); 

        BicicletaDTO resultado = service.atualizarBicicleta(ID, requestDTO);

        assertNotNull(resultado);
        verify(mapper, times(1)).updateEntityFromDto(requestDTO, bicicleta); 
        verify(repository, times(1)).save(bicicleta);
    }

    @SuppressWarnings("null")
    @Test
    void atualizarBicicletaIdNull() {
         Throwable exception = assertThrows(NegocioException.class, () -> service.atualizarBicicleta(null, requestDTO));
        assertNotNull(exception); 
    }
    
    @SuppressWarnings("null")
    @Test
    void atualizarBicicletaException() {
        when(repository.findById(ID_INVALIDO)).thenReturn(Optional.empty());

        ObjectNotFoundException thrown = assertThrows(ObjectNotFoundException.class, () -> {
            service.atualizarBicicleta(ID_INVALIDO, requestDTO);
        });

        verify(repository, times(1)).findById(ID_INVALIDO);
        verify(mapper, never()).updateEntityFromDto(any(), any()); 
        assertTrue(thrown.getMessage().contains("Bicicleta não encontrada"));

    }
    
    /* DELETAR BICICLETA  */

    @SuppressWarnings("null")
    @Test
    void deletarBicicleta() {
        service.deletarBicicleta(ID);

        verify(repository, times(1)).deleteById(ID); 
    }

    @SuppressWarnings("null")
    @Test
    void deletarBicicletaNull() {
        Throwable exception = assertThrows(NegocioException.class, () -> service.deletarBicicleta(null));
        assertNotNull(exception); 
    }
}