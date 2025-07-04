package unirio.pm.external_service.services.implamentation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import unirio.pm.external_service.client.cartao.CartaoClient;
import unirio.pm.external_service.client.paypal.PaypalClient;
import unirio.pm.external_service.dto.CartaoDTO;
import unirio.pm.external_service.dto.CobrancaDTO;
import unirio.pm.external_service.dto.CobrancaRequestDTO;
import unirio.pm.external_service.entity.Cobranca;
import unirio.pm.external_service.entity.FilaCobranca;
import unirio.pm.external_service.enumerations.StatusCobranca;
import unirio.pm.external_service.exception.ObjectNotFoundException;
import unirio.pm.external_service.exception.cobranca.PaypalApiException;
import unirio.pm.external_service.exception.cobranca.PaypalApiException.PaypalErrorDetail;
import unirio.pm.external_service.mapper.CobrancaMapper;
import unirio.pm.external_service.repository.CobrancaRepository;
import unirio.pm.external_service.repository.FilaCobrancaRepository;
import unirio.pm.external_service.services.CobrancaService;

@Service
public class CobrancaServiceImplementation implements CobrancaService{
    
    private final CobrancaRepository cobrancaRepository;
    private final FilaCobrancaRepository filaRepository;
    private final PaypalClient paypalClient;
    private final CartaoClient cartaoClient;
    private final CobrancaMapper mapper;


    public CobrancaServiceImplementation(
        CobrancaRepository cobrancaRepository, 
        FilaCobrancaRepository filaRepository,
        PaypalClient paypalClient, 
        CartaoClient cartaoClient,
        CobrancaMapper mapper) {
        this.cobrancaRepository = cobrancaRepository;
        this.filaRepository = filaRepository;
        this.paypalClient = paypalClient;
        this.cartaoClient = cartaoClient;
        this.mapper = mapper;
    }

    @Override
    public CobrancaDTO criarCobranca(CobrancaRequestDTO cobranca){

        CartaoDTO cartao = cartaoClient.buscarCartao(cobranca.getCiclista());

        Cobranca entity = new Cobranca();
       
        try{

            entity.setHoraSolicitacao(LocalDateTime.now());

            paypalClient.autorizarTransacao(cartao, cobranca.getValor());

            entity.setValor(cobranca.getValor());
            entity.setCiclista(cobranca.getCiclista());
            entity.setStatus(StatusCobranca.PAGA);
            entity.setHoraFinalizacao(LocalDateTime.now());

            return mapper.toDTO((cobrancaRepository.save(entity)));

        } catch (PaypalApiException e){
            if (isErroCartao(e)) 
               filaRepository.save(new FilaCobranca(cobranca.getValor(), cobranca.getCiclista()));
        
            throw e;
        }
    }

    @Override
    public List<CobrancaDTO> processarFilaCobranca() {
        List<CobrancaDTO> cobrancasProcessadas = new ArrayList<>();

        filaRepository.findAll().forEach(fila -> {
            CartaoDTO cartao = cartaoClient.buscarCartaoCerto(fila.getCiclista());

            if (cartao == null) 
                return;

            Cobranca cobranca = new Cobranca();

            try {

                cobranca.setHoraSolicitacao(LocalDateTime.now());
                paypalClient.autorizarTransacao(cartao, fila.getValor());

                cobranca.setValor(fila.getValor());
                cobranca.setCiclista(fila.getCiclista());
                cobranca.setStatus(StatusCobranca.PAGA);
                cobranca.setHoraFinalizacao(LocalDateTime.now());

                Cobranca cobrancaSalva = cobrancaRepository.save(cobranca);
                filaRepository.delete(fila);

                cobrancasProcessadas.add(mapper.toDTO((cobrancaSalva)));

            } catch (PaypalApiException e) {
                if (!isErroCartao(e)) 
                    throw e;
            }
        });

        return cobrancasProcessadas;
    }

    @Override
    public CobrancaDTO buscarCobranca(Long id){
        Cobranca cobranca = cobrancaRepository.findById(id)
                            .orElseThrow(() -> 
                            new ObjectNotFoundException("Not Found", id));
        return mapper.toDTO((cobranca));
    }

    private boolean isErroCartao(PaypalApiException e){
        return e.getDetails()
        .stream()
        .map(PaypalErrorDetail::getIssue)          
        .anyMatch(issue -> issue.contains("CARD"));
    }
}
