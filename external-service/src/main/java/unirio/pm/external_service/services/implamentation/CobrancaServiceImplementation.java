package unirio.pm.external_service.services.implamentation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import unirio.pm.external_service.client.cartao.CartaoClient;
import unirio.pm.external_service.client.paypal.PaypalClient;
import unirio.pm.external_service.dto.CartaoDTO;
import unirio.pm.external_service.dto.CobrancaDTO;
import unirio.pm.external_service.dto.CobrancaRequestDTO;
import unirio.pm.external_service.entity.Cobranca;
import unirio.pm.external_service.entity.FilaCobranca;
import unirio.pm.external_service.enumerations.StatusCobranca;
import unirio.pm.external_service.exception.cobranca.PaypalApiException;
import unirio.pm.external_service.exception.cobranca.PaypalApiException.PaypalErrorDetail;
import unirio.pm.external_service.repository.CobrancaRepository;
import unirio.pm.external_service.repository.FilaCobrancaRepository;
import unirio.pm.external_service.services.CobrancaService;

@Service
public class CobrancaServiceImplementation implements CobrancaService{
    
    @Autowired
    private CobrancaRepository cobrancaRepository;

    @Autowired
    private FilaCobrancaRepository filaRepository;

    @Autowired
    private PaypalClient paypalClient;

    @Autowired
    private CartaoClient cartaoClient;

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

            return toDTO(cobrancaRepository.save(entity));

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

                cobrancasProcessadas.add(toDTO(cobrancaSalva));

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
        return toDTO(cobranca);
    }

    private boolean isErroCartao(PaypalApiException e){
        return e.getDetails()
        .stream()
        .map(PaypalErrorDetail::getIssue)          
        .anyMatch(issue -> issue.contains("CARD"));
    }

    private CobrancaDTO toDTO(Cobranca cobranca) {
        return new CobrancaDTO(
            cobranca.getId(),
            cobranca.getValor(),
            cobranca.getCiclista(),
            cobranca.getStatus(),
            cobranca.getHoraSolicitacao(),
            cobranca.getHoraFinalizacao()
        );
    }
}
