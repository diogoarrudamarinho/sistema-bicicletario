package unirio.pm.external_service.services.implamentation;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import unirio.pm.external_service.client.cartao.CartaoClient;
import unirio.pm.external_service.client.paypal.PaypalClient;
import unirio.pm.external_service.dto.CartaoDTO;
import unirio.pm.external_service.dto.CobrancaDTO;
import unirio.pm.external_service.dto.CobrancaRequestDTO;
import unirio.pm.external_service.entity.Cobranca;
import unirio.pm.external_service.enumerations.StatusCobranca;
import unirio.pm.external_service.repository.CobrancaRepository;
import unirio.pm.external_service.services.CobrancaService;

@Service
public class CobrancaServiceImplementation implements CobrancaService{
    
    @Autowired
    private CobrancaRepository cobrancaRepository;

    //@Autowired
    //private FilaCobrancaRepository filaRepository;

    @Autowired
    private PaypalClient paypalClient;

    @Autowired
    private CartaoClient cartaoClient;

    @Override
    public CobrancaDTO criarCobranca(CobrancaRequestDTO cobranca){

        CartaoDTO cartao = cartaoClient.buscarCartao(cobranca.getCiclista());
        Cobranca entity = new Cobranca();

        paypalClient.autorizarTransacao(cartao, cobranca.getValor());
        entity.setStatus(StatusCobranca.PAGA);
        entity.setValor(cobranca.getValor());
        entity.setCiclista(cobranca.getCiclista());
        entity.setHoraFinalizacao(LocalDateTime.now());

        return toDTO(cobrancaRepository.save(entity));

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
