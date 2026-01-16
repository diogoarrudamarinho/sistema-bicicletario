package dev.unirio.equipmentservice.service;

import java.util.List;
import java.util.Optional;

import org.hibernate.ObjectNotFoundException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.unirio.equipmentservice.dto.TotemDTO;
import dev.unirio.equipmentservice.dto.TotemRequestDTO;
import dev.unirio.equipmentservice.entity.Totem;
import dev.unirio.equipmentservice.exception.NegocioException;
import dev.unirio.equipmentservice.mapper.TotemMapper;
import dev.unirio.equipmentservice.repository.TotemRepository;
import dev.unirio.equipmentservice.service.implementation.TotemServiceImplementation;

@ExtendWith(MockitoExtension.class)
class TotemServiceTest {

    private static final Long ID = 1L;

    @Mock
    private TotemRepository repository;

    @Mock
    private TotemMapper mapper;

    @InjectMocks
    private TotemServiceImplementation service;

    private Totem totem;
    private TotemDTO totemDTO;
    private TotemRequestDTO requestDTO;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        totem = new Totem();
        totemDTO = new TotemDTO();
        requestDTO = new TotemRequestDTO();
    }

    /* =========================
       buscarTotem
       ========================= */

    @Test
    void buscarTotemIdNull() {
        Throwable ex = assertThrows(
            NegocioException.class,
            () -> service.buscarTotem(null)
        );

        assertNotNull(ex);
        verify(repository, never()).findById(any());
    }

    @Test
    void buscarTotemNotFound() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        Throwable ex = assertThrows(
            ObjectNotFoundException.class,
            () -> service.buscarTotem(ID)
        );

        assertNotNull(ex);
    }

    @Test
    void buscarTotemSucesso() {
        when(repository.findById(ID)).thenReturn(Optional.of(totem));
        when(mapper.toDto(totem)).thenReturn(totemDTO);

        TotemDTO result = service.buscarTotem(ID);

        assertNotNull(result);
        verify(repository).findById(ID);
        verify(mapper).toDto(totem);
    }

    /* =========================
       buscarTotens
       ========================= */

    @Test
    void buscarTotensSucesso() {
        when(repository.findAll()).thenReturn(List.of(totem));
        when(mapper.toDtoList(any())).thenReturn(List.of(totemDTO));

        List<TotemDTO> result = service.buscarTotens();

        assertEquals(1, result.size());
        verify(repository).findAll();
        verify(mapper).toDtoList(any());
    }

    /* =========================
       criarTotem
       ========================= */

    @Test
    void criarTotemNull() {
        Throwable ex = assertThrows(
            NegocioException.class,
            () -> service.criarTotem(null)
        );

        assertNotNull(ex);
        verify(repository, never()).save(any());
    }

    @Test
    void criarTotemSucesso() {
        when(mapper.toEntity(requestDTO)).thenReturn(totem);
        when(repository.save(totem)).thenReturn(totem);
        when(mapper.toDto(totem)).thenReturn(totemDTO);

        TotemDTO result = service.criarTotem(requestDTO);

        assertNotNull(result);
        verify(repository).save(totem);
    }

    /* =========================
       atualizarTotem
       ========================= */

    @Test
    void atualizarTotemIdNull() {
        Throwable ex = assertThrows(
            NegocioException.class,
            () -> service.atualizarTotem(null, requestDTO)
        );

        assertNotNull(ex);
        verify(repository, never()).findById(any());
    }

    @Test
    void atualizarTotemNotFound() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        Throwable ex = assertThrows(
            ObjectNotFoundException.class,
            () -> service.atualizarTotem(ID, requestDTO)
        );

        assertNotNull(ex);
    }

    @Test
    void atualizarTotemSucesso() {
        when(repository.findById(ID)).thenReturn(Optional.of(totem));
        when(repository.save(totem)).thenReturn(totem);
        when(mapper.toDto(totem)).thenReturn(totemDTO);

        TotemDTO result = service.atualizarTotem(ID, requestDTO);

        assertNotNull(result);
        verify(mapper).updateEntityFromDto(requestDTO, totem);
        verify(repository).save(totem);
    }

    /* =========================
       deletarTotem
       ========================= */

    @Test
    void deletarTotemIdNull() {
        Throwable ex = assertThrows(
            NegocioException.class,
            () -> service.deletarTotem(null)
        );

        assertNotNull(ex);
        verify(repository, never()).existsById(any());
    }

    @Test
    void deletarTotemNotFound() {
        when(repository.existsById(ID)).thenReturn(false);

        Throwable ex = assertThrows(
            ObjectNotFoundException.class,
            () -> service.deletarTotem(ID)
        );

        assertNotNull(ex);
    }

    @Test
    void deletarTotemSucesso() {
        when(repository.existsById(ID)).thenReturn(true);

        service.deletarTotem(ID);

        verify(repository).deleteById(ID);
    }
}
