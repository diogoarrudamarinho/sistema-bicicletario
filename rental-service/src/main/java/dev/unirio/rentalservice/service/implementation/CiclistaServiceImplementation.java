package dev.unirio.rentalservice.service.implementation;

import java.util.UUID;

import org.hibernate.ObjectNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import dev.unirio.rentalservice.client.EquipmentClient;
import dev.unirio.rentalservice.client.ExternalClient;
import dev.unirio.rentalservice.dto.BicicletaDTO;
import dev.unirio.rentalservice.dto.CiclistaDTO;
import dev.unirio.rentalservice.dto.CiclistaRequestDTO;
import dev.unirio.rentalservice.dto.EmailDTO;
import dev.unirio.rentalservice.entity.Ciclista;
import dev.unirio.rentalservice.entity.TokenConfirmacao;
import dev.unirio.rentalservice.enumeration.CiclistaStatus;
import dev.unirio.rentalservice.enumeration.Nacionalidade;
import dev.unirio.rentalservice.mapper.CiclistaMapper;
import dev.unirio.rentalservice.repository.CiclistaRepository;
import dev.unirio.rentalservice.repository.TokenConfirmacaoRepository;
import dev.unirio.rentalservice.service.AluguelService;

public class CiclistaServiceImplementation /*implements CiclistaService*/ {
    
    private static final String NOT_FOUND = "Ciclista não encontrado";

    private final CiclistaRepository repository;
    private final CiclistaMapper mapper;

    private final AluguelService aluguelService;

    private final TokenConfirmacaoRepository tokenRepository;
    
    private final ExternalClient externalclient;
    private final EquipmentClient equipmentClient;

    
    public CiclistaServiceImplementation(CiclistaRepository repository, CiclistaMapper mapper,
            ExternalClient externalclient, EquipmentClient equipmentClient, AluguelService aluguelService,
            TokenConfirmacaoRepository tokenRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.aluguelService = aluguelService;
        this.externalclient = externalclient;
        this.equipmentClient = equipmentClient;
        this.tokenRepository = tokenRepository;
    }


    //@Override
    public CiclistaDTO buscarCiclista(Long id){        
        return mapper.toDto(
            repository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException(NOT_FOUND, id))
        );
    }

    // @Override
    public BicicletaDTO buscarBicicleta(Long id){        
        return equipmentClient.getBicicleta(
            aluguelService.buscarBicicletaAlugada(id));
    }

    // @Override
    public boolean permiteAluguel(Long id){
        return !aluguelService.ciclistaComAluguelAtivo(id);
    }

    // @Override
    @Transactional
    public CiclistaDTO criarCiclista(CiclistaRequestDTO dto) {
        
        validarDocumentacao(dto);
        
        externalclient.validarCartao(dto.cartao());

        Ciclista ciclista = mapper.toEntityFromRequest(dto);

        if (ciclista.getCartao() != null) 
            ciclista.getCartao().setCiclista(ciclista);

        ciclista.setStatus(CiclistaStatus.AGUARDANDO_CONFIRMACAO);

        Ciclista newCiclista = repository.save(ciclista);

        enviarEmailConfirmacao(newCiclista);
        
        return mapper.toDto(newCiclista);
    }

    private void validarDocumentacao(CiclistaRequestDTO dto) {
        if (dto.nacionalidade() == Nacionalidade.BRASILEIRO) {
            if (dto.cpf() == null || dto.cpf().isBlank()) 
                throw new IllegalArgumentException("CPF é obrigatório para brasileiros."); 
        } else if (dto.passaporte() == null) 
                throw new IllegalArgumentException("Passaporte é obrigatório para estrangeiros."); 
    }

    private void enviarEmailConfirmacao(Ciclista ciclista){
        String token = UUID.randomUUID().toString();
        tokenRepository.save(new TokenConfirmacao(token, ciclista));

        String link = String.format("http://localhost:8082/ciclista/%d/ativar", ciclista.getId());

        String mensagem = """
            Olá %s, seja bem-vindo!

            Sua conta foi criada com sucesso. \
            Clique no link abaixo para confirmar seu cadastro:

            %s

            O link expira em 24 horas.
            """.formatted(ciclista.getNome(), link);

        EmailDTO emailRequest = new EmailDTO(
                ciclista.getEmail(), 
                "Confirmação de Cadastro", 
                mensagem);

        externalclient.postEmail(emailRequest);
    }
}
