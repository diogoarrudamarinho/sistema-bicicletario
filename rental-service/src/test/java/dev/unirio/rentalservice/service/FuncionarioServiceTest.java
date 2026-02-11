package dev.unirio.rentalservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.unirio.rentalservice.dto.FuncionarioDTO;
import dev.unirio.rentalservice.entity.Funcionario;
import dev.unirio.rentalservice.mapper.FuncionarioMapper;
import dev.unirio.rentalservice.repository.FuncionarioRepository;
import dev.unirio.rentalservice.service.implementation.FuncionarioServiceImplementation;

@ExtendWith(MockitoExtension.class)
class FuncionarioServiceTest {
    @Mock private FuncionarioRepository repository;
    @Mock private FuncionarioMapper mapper;

    @InjectMocks
    private FuncionarioServiceImplementation service;

    private Funcionario funcionario;
    private FuncionarioDTO funcionarioDTO;
    private final Long ID = 1L;

    @BeforeEach
    void setUp() {
        funcionario = new Funcionario();
        funcionario.setId(ID);
        funcionario.setNome("Beltrano");

        funcionarioDTO = new FuncionarioDTO(ID, "123", "123", "bel@email.com", "Beltrano", "25", "Reparador", "99999999999");
    }

    // --- TESTES DA FUNÇÃO: buscarFuncionario ---

    @Test
    void buscarFuncionario() {
        when(repository.findById(ID)).thenReturn(Optional.of(funcionario));
        when(mapper.toDto(funcionario)).thenReturn(funcionarioDTO);

        FuncionarioDTO resultado = service.buscarFuncionario(ID);

        assertNotNull(resultado);
        verify(repository).findById(ID);
    }

    @Test
    void buscarFuncionarioFuncionarioNaoEncontrado() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.buscarFuncionario(ID));
    }

    // --- TESTES DA FUNÇÃO: buscarFuncionarios ---

    @Test
    void buscarFuncionarios() {
        when(repository.findAll()).thenReturn(List.of(funcionario));
        when(mapper.toDto(any())).thenReturn(funcionarioDTO);

        List<FuncionarioDTO> resultado = service.buscarFuncionarios();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(repository).findAll();
    }

    // --- TESTES DA FUNÇÃO: criarFuncionario ---

    @Test
    void criarFuncionario() {
        when(mapper.toEntity(funcionarioDTO)).thenReturn(funcionario);
        when(repository.save(funcionario)).thenReturn(funcionario);
        when(mapper.toDto(funcionario)).thenReturn(funcionarioDTO);

        FuncionarioDTO resultado = service.criarFuncionario(funcionarioDTO);

        assertNotNull(resultado);
        verify(repository).save(any());
    }

    // --- TESTES DA FUNÇÃO: atualizarFuncionario ---

    @Test
    void atualizarFuncionario() {
        when(repository.findById(ID)).thenReturn(Optional.of(funcionario));
        when(repository.save(funcionario)).thenReturn(funcionario);
        when(mapper.toDto(funcionario)).thenReturn(funcionarioDTO);

        FuncionarioDTO resultado = service.atualizarFuncionario(ID, funcionarioDTO);

        assertNotNull(resultado);
        verify(mapper).updateEntityFromDto(funcionarioDTO, funcionario);
        verify(repository).save(funcionario);
    }

    @Test
    void atualizarFuncionarioFuncionarioNaoEncontrado() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.atualizarFuncionario(ID, funcionarioDTO));
        verify(repository, never()).save(any());
    }

    // --- TESTES DA FUNÇÃO: deletarFuncionario ---

    @Test
    void deletarFuncionario() {
        when(repository.existsById(ID)).thenReturn(true);

        service.deletarFuncionario(ID);

        verify(repository).deleteById(ID);
    }

    @Test
    void deletarFuncionarioFuncionarioNaoEncontrado() {
        when(repository.existsById(ID)).thenReturn(false);

        assertThrows(ObjectNotFoundException.class, () -> service.deletarFuncionario(ID));
        verify(repository, never()).deleteById(any());
    }
}
