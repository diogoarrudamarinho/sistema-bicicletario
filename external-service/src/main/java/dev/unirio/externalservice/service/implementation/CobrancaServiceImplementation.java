package dev.unirio.externalservice.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import dev.unirio.externalservice.client.paypal.PaypalClient;
import dev.unirio.externalservice.client.paypal.cartao.CartaoClient;
import dev.unirio.externalservice.dto.CartaoDTO;
import dev.unirio.externalservice.dto.CobrancaDTO;
import dev.unirio.externalservice.dto.CobrancaRequestDTO;
import dev.unirio.externalservice.entity.Cobranca;
import dev.unirio.externalservice.enumeration.StatusCobranca;
import dev.unirio.externalservice.exception.ObjectNotFoundException;
import dev.unirio.externalservice.exception.cobranca.PaypalApiException;
import dev.unirio.externalservice.exception.cobranca.PaypalApiException.PaypalErrorDetail;
import dev.unirio.externalservice.mapper.CobrancaMapper;
import dev.unirio.externalservice.repository.CobrancaRepository;
import dev.unirio.externalservice.service.CobrancaService;

@Service
public class CobrancaServiceImplementation implements CobrancaService{

    private final CobrancaRepository repository;
    private final PaypalClient paypalClient;
    private final CartaoClient cartaoClient;
    private final CobrancaMapper mapper;

    public CobrancaServiceImplementation(
        CobrancaRepository repository, 
        PaypalClient paypalClient, 
        CartaoClient cartaoClient,
        CobrancaMapper mapper) {
        this.repository = repository;
        this.paypalClient = paypalClient;
        this.cartaoClient = cartaoClient;
        this.mapper = mapper;
    }

    @Override
    public CobrancaDTO criarCobranca(CobrancaRequestDTO cobranca){
        
        CartaoDTO cartao = cartaoClient.buscarCartao(cobranca.getCiclista());

        Cobranca novaCobranca = new Cobranca();
        novaCobranca.setCiclista(cobranca.getCiclista());
        novaCobranca.setValor(cobranca.getValor());

        try {

            paypalClient.autorizarTransacao(cartao, cobranca.getValor());

            novaCobranca.setStatus(StatusCobranca.PAGA);
            novaCobranca.setHoraFinalizacao(LocalDateTime.now());

            return mapper.toDTO(repository.save(novaCobranca));

        } catch (PaypalApiException e) {

            if (!isCardError(e)) 
                throw e;
    
            novaCobranca.setStatus(StatusCobranca.FALHA);

            return mapper.toDTO(repository.save(novaCobranca));
        }
    } 

    @Override
    public CobrancaDTO buscarCobranca(Long id){
        return id == null ? 
        null :
        mapper.toDTO(
            repository.findById(id)
                .orElseThrow(() -> 
                new ObjectNotFoundException("Cobrança não encontrada", id)));
    } 

    @Override
    public List<CobrancaDTO> processarFilaCobranca() {
        
        return repository.findAllByStatus(StatusCobranca.FALHA)
        .stream()
        .map(cobranca -> {

            CartaoDTO cartao = cartaoClient.buscarCartao(cobranca.getCiclista());

            if (cartao == null) 
                return null;

            try {

                paypalClient.autorizarTransacao(cartao, cobranca.getValor());
                
                cobranca.setStatus(StatusCobranca.PAGA);
                cobranca.setHoraFinalizacao(LocalDateTime.now());
                return mapper.toDTO(repository.save(cobranca));

            } catch (PaypalApiException e) {
                
                if (!isCardError(e))
                    throw e;
               
                return null; 
            }
        })
        .filter(Objects::nonNull)
        .toList();
    }

    @Override
    public void deletar(Long id){
        if (!repository.existsById(id)) 
            throw new ObjectNotFoundException("Cobrança não encontrada", id);
        
        repository.deleteById(id);
    }

    private boolean isCardError(PaypalApiException e){
        return e.getDetails()
        .stream()
        .map(PaypalErrorDetail::getIssue)          
        .anyMatch(issue -> issue.contains("CARD"));
    }
}
