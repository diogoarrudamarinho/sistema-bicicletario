package dev.unirio.rentalservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cglib.core.Local;

import dev.unirio.rentalservice.client.EquipmentClient;
import dev.unirio.rentalservice.client.ExternalClient;
import dev.unirio.rentalservice.dto.BicicletaDTO;
import dev.unirio.rentalservice.dto.CiclistaDTO;
import dev.unirio.rentalservice.dto.CiclistaRequestDTO;
import dev.unirio.rentalservice.dto.PassaporteDTO;
import dev.unirio.rentalservice.entity.Aluguel;
import dev.unirio.rentalservice.entity.Ciclista;
import dev.unirio.rentalservice.entity.TokenConfirmacao;
import dev.unirio.rentalservice.enumeration.CiclistaStatus;
import dev.unirio.rentalservice.enumeration.Nacionalidade;
import dev.unirio.rentalservice.mapper.CiclistaMapper;
import dev.unirio.rentalservice.repository.AluguelRepository;
import dev.unirio.rentalservice.repository.CiclistaRepository;
import dev.unirio.rentalservice.repository.TokenConfirmacaoRepository;
import dev.unirio.rentalservice.service.implementation.CiclistaServiceImplementation;

@ExtendWith(MockitoExtension.class)
class CiclistaServiceTest {
    @Mock private CiclistaRepository repository;
    @Mock private CiclistaMapper mapper;
    @Mock private AluguelRepository aluguelRepository;
    @Mock private TokenConfirmacaoRepository tokenRepository;
    @Mock private ExternalClient externalClient;
    @Mock private EquipmentClient equipmentClient;

    @InjectMocks
    private CiclistaServiceImplementation service;

    private Ciclista ciclista;
    private CiclistaDTO ciclistaDTO;
    private CiclistaRequestDTO requestBrasileiro;
    private CiclistaRequestDTO requestEstrangeiro;

    @BeforeEach
    void setUp() {
        ciclista = new Ciclista();
        ciclista.setId(1L);
        ciclista.setNome("Diogo");
        ciclista.setEmail("diogo@email.com");

        ciclistaDTO = new CiclistaDTO(1L, "Diogo", "diogo@email.com", null, null, null, null, null, null);
        
        requestBrasileiro = new CiclistaRequestDTO("João Silva", "joao.silva@email.com", "12345678901", "senha123", Nacionalidade.BRASILEIRO, LocalDate.of(1995, 5, 10), null, "abc", null);
        requestEstrangeiro = new CiclistaRequestDTO( "Maria Oliveira","maria.oliveira@email.com","98765432100","abc@123",Nacionalidade.ESTRANGEIRO ,LocalDate.of(2000, 8, 22),null, "abc", null);
    }

    // --- buscarCiclista ---
    @Test
    void buscarCiclista() {
        when(repository.findById(1L)).thenReturn(Optional.of(ciclista));
        when(mapper.toDto(ciclista)).thenReturn(ciclistaDTO);

        CiclistaDTO result = service.buscarCiclista(1L);
        assertNotNull(result);
    }

    @Test
    void buscarCiclistaCiclistaNaoEncontrado() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ObjectNotFoundException.class, () -> service.buscarCiclista(1L));
    }

    // --- permiteAluguel ---
    @Test
    void permiteAluguel() {
        when(aluguelRepository.existsByCiclistaAndHoraFimIsNull(1L)).thenReturn(false);
        assertTrue(service.permiteAluguel(1L));
    }

    // --- criarCiclista (Branches de Validação) ---
    @Test
    void criarCiclista() {
        when(repository.existsByEmail(any())).thenReturn(false);
        when(mapper.toEntityFromRequest(any())).thenReturn(ciclista);
        when(repository.save(any())).thenReturn(ciclista);
        when(mapper.toDto(any())).thenReturn(ciclistaDTO);

        CiclistaDTO result = service.criarCiclista(requestBrasileiro);

        assertNotNull(result);
        verify(externalClient).validarCartao(any());
        verify(externalClient).postEmail(any()); 
    }

    @Test
    void criarCiclistaEmailJáCadastrado() {
        when(repository.existsByEmail(any())).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> service.criarCiclista(requestBrasileiro));
    }

    @Test
    void criarCiclistaBrasileiroSemCpf() {
        CiclistaRequestDTO erroCpf = new CiclistaRequestDTO("Diogo", null, null, "",  Nacionalidade.BRASILEIRO, LocalDate.now(), null, null, null);
        assertThrows(IllegalArgumentException.class, () -> service.criarCiclista(erroCpf));
    }

    @Test
    void criarCiclistaEstrangeiroSemPassaporte() {
        CiclistaRequestDTO erroPassa = new CiclistaRequestDTO("John", "", "", "", Nacionalidade.ESTRANGEIRO, LocalDate.now(), null, null, null);
        assertThrows(IllegalArgumentException.class, () -> service.criarCiclista(erroPassa));
    }

    // --- ativarCadastro ---
    @Test
    void ativarCadastro() {
        TokenConfirmacao token = spy(new TokenConfirmacao("raw-token", ciclista));
        when(tokenRepository.findByToken("raw-token")).thenReturn(Optional.of(token));
        when(token.isExpired()).thenReturn(false);
        when(mapper.toDto(ciclista)).thenReturn(ciclistaDTO);

        CiclistaDTO result = service.ativarCadastro(1L, "raw-token");

        assertEquals(CiclistaStatus.ATIVO, ciclista.getStatus());
        assertNotNull(result);
    }

    @Test
    void ativarCadastroTokenExpirado() {
        TokenConfirmacao token = spy(new TokenConfirmacao("old-token", ciclista));
        when(tokenRepository.findByToken("old-token")).thenReturn(Optional.of(token));
        when(token.isExpired()).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.ativarCadastro(1L, "old-token"));
    }

    @Test
    void ativarCadastroTokenNaoEncontrado() {
        when(tokenRepository.findByToken(any())).thenReturn(Optional.empty());
        assertThrows(ObjectNotFoundException.class, () -> service.ativarCadastro(1L, "token"));
    }

    // --- desativarCadastro ---
    @Test
    void desativarCadastro() {
        when(repository.findById(1L)).thenReturn(Optional.of(ciclista));
        service.desativarCadastro(1L);
        assertEquals(CiclistaStatus.INATIVO, ciclista.getStatus());
        verify(repository).save(ciclista);
    }

    // --- buscarBicicleta ---
    @Test
    void buscarBicicleta() {
        Aluguel aluguel = new Aluguel();
        aluguel.setBicicleta(55L);
        when(aluguelRepository.findByCiclistaAndHoraFimIsNull(1L)).thenReturn(Optional.of(aluguel));
        when(equipmentClient.getBicicleta(55L)).thenReturn(new BicicletaDTO(null, null, null, null, null, null));

        assertNotNull(service.buscarBicicleta(1L));
    }

    // --- atualizarCiclista ---

    @Test
    void atualizarCiclista() {
        when(repository.findById(1L)).thenReturn(Optional.of(ciclista));
        when(repository.save(any(Ciclista.class))).thenReturn(ciclista);
        when(mapper.toDto(any(Ciclista.class))).thenReturn(ciclistaDTO);

        CiclistaDTO resultado = service.atualizarCiclista(1L, requestBrasileiro);

        assertNotNull(resultado);
        verify(mapper).updateEntityFromDto(requestBrasileiro, ciclista);
        verify(repository).save(ciclista);
    }

    @Test
    void atualizarCiclistaCiclistaNaoEncontrado() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, 
            () -> service.atualizarCiclista(1L, requestBrasileiro));
        
        verify(repository, never()).save(any());
        verify(mapper, never()).updateEntityFromDto(any(), any());
    }
}
