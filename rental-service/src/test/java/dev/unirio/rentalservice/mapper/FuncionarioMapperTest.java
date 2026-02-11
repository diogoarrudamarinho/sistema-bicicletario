package dev.unirio.rentalservice.mapper;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.unirio.rentalservice.dto.FuncionarioDTO;
import dev.unirio.rentalservice.entity.Funcionario;

@ExtendWith(MockitoExtension.class)
public class FuncionarioMapperTest {
    
    private final FuncionarioMapperImpl mapper = new FuncionarioMapperImpl();

    private Funcionario entity;
    private FuncionarioDTO dto;
    private final Long ID = 1L;

    @BeforeEach
    void setUp() {
        entity = new Funcionario();
        entity.setId(ID);
        entity.setSenha("123");
        entity.setConfirmacaoSenha("123");
        entity.setEmail("beltrano@email.com");
        entity.setNome("Beltrano");
        entity.setIdade("25");
        entity.setFuncao("Reparador");
        entity.setCpf("99999999999");

        dto = new FuncionarioDTO(ID, "123", "123", "beltrano@email.com", "Beltrano", "25", "Reparador", "99999999999");
    }

    // --- TESTES DA FUNÇÃO: toDto ---

    @Test
    void toDto() {
        FuncionarioDTO result = mapper.toDto(entity);

        assertNotNull(result);
        assertEquals(entity.getId(), result.id());
        assertEquals(entity.getNome(), result.nome());
        assertEquals(entity.getEmail(), result.email());
        assertEquals(entity.getFuncao(), result.funcao());
    }

    @Test
    void toDtoNull() {
        assertNull(mapper.toDto(null));
    }

    // --- TESTES DA FUNÇÃO: toEntity ---

    @Test
    void toEntity() {
        Funcionario result = mapper.toEntity(dto);

        assertNotNull(result);
        assertEquals(dto.id(), result.getId());
        assertEquals(dto.senha(), result.getSenha());
        assertEquals(dto.cpf(), result.getCpf());
        assertEquals(dto.funcao(), result.getFuncao());
    }

    @Test
    void toEntityNull() {
        assertNull(mapper.toEntity(null));
    }

    // --- TESTES DA FUNÇÃO: updateEntityFromDto ---

    @Test
    void updateEntityFromDto() {
        Funcionario targetEntity = new Funcionario();
        
        mapper.updateEntityFromDto(dto, targetEntity);

        assertEquals(dto.nome(), targetEntity.getNome());
        assertEquals(dto.email(), targetEntity.getEmail());
        assertEquals(dto.funcao(), targetEntity.getFuncao());
        assertEquals(dto.senha(), targetEntity.getSenha());
    }

    @Test
    void updateEntityFromDtoNull() {
        Funcionario targetEntity = new Funcionario();
        targetEntity.setNome("Original");

        mapper.updateEntityFromDto(null, targetEntity);

        assertEquals("Original", targetEntity.getNome());
    }
}
