package dev.unirio.rentalservice.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.unirio.rentalservice.dto.CartaoDTO;
import dev.unirio.rentalservice.entity.Cartao;
import dev.unirio.rentalservice.mapper.CartaoMapper;
import dev.unirio.rentalservice.repository.CartaoRepository;
import dev.unirio.rentalservice.service.implementation.CartaoServiceImplementation;

@ExtendWith(MockitoExtension.class)
class CartaoServiceTest {
    
    @Mock private CartaoRepository repository;
    @Mock private CartaoMapper mapper;

    @InjectMocks
    private CartaoServiceImplementation service;

    private Cartao cartao;
    private CartaoDTO cartaoDTO;
    private final Long CICLISTA_ID = 1L;

    @BeforeEach
    void setUp() {
        cartao = new Cartao();
        cartaoDTO = new CartaoDTO(1L, "Fulano", "123456789", null, "123");
    }

    // --- TESTES DA FUNÇÃO: buscarCartao ---

    @Test
    void buscarCartao() {
        when(repository.findByCiclistaId(CICLISTA_ID)).thenReturn(Optional.of(cartao));
        when(mapper.toDto(cartao)).thenReturn(cartaoDTO);

        CartaoDTO resultado = service.buscarCartao(CICLISTA_ID);

        assertNotNull(resultado);
        verify(repository).findByCiclistaId(CICLISTA_ID);
        verify(mapper).toDto(cartao);
    }

    @Test
    void buscarCartaoCartaoNaoEncontrado() {
        when(repository.findByCiclistaId(CICLISTA_ID)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.buscarCartao(CICLISTA_ID));
        verify(mapper, never()).toDto(any());
    }

    // --- TESTES DA FUNÇÃO: atualizarCartao ---

    @Test
    void atualizarCartao() {
        when(repository.findByCiclistaId(CICLISTA_ID)).thenReturn(Optional.of(cartao));
        when(repository.save(cartao)).thenReturn(cartao);
        when(mapper.toDto(cartao)).thenReturn(cartaoDTO);

        CartaoDTO resultado = service.atualizarCartao(CICLISTA_ID, cartaoDTO);

        assertNotNull(resultado);
        verify(mapper).updateEntityFromDto(cartaoDTO, cartao);
        verify(repository).save(cartao);
    }

    @Test
    void atualizarCartaoCartaoNaoEncontrado() {
        when(repository.findByCiclistaId(CICLISTA_ID)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.atualizarCartao(CICLISTA_ID, cartaoDTO));
        
        verify(mapper, never()).updateEntityFromDto(any(), any());
        verify(repository, never()).save(any());
    }
}
