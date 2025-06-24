package unirio.pm.external_service.services.implamentation;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import unirio.pm.external_service.client.CartaoClient;
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

    @Autowired
    private PaypalClient paypalClient;

    @Autowired
    private CartaoClient cartaoClient;

    @Override
    public CobrancaDTO criarCobranca(CobrancaRequestDTO cobranca){

        CartaoDTO cartao;

        try {
            cartao = cartaoClient.buscarCartao(cobranca.getCiclista());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar cartão: " + e.getMessage());
        }

        boolean sucesso;

        try {
            sucesso = paypalClient.autorizarTransacao(cartao, cobranca.getValor());
        } catch (Exception e) {
            throw new RuntimeException("Erro no pagamento: " + e.getMessage());
        }

        Cobranca entity = new Cobranca();

        if (!sucesso) {
            entity.setStatus(StatusCobranca.PENDENTE);
            entity.setValor(cobranca.getValor());
            entity.setCiclista(cobranca.getCiclista());
        } else {
            entity.setStatus(StatusCobranca.PAGA);
            entity.setValor(cobranca.getValor());
            entity.setCiclista(cobranca.getCiclista());
            entity.setHoraFinalizacao(LocalDate.now());
        }

        return toDTO(cobrancaRepository.save(new Cobranca()));
    }

    @Override
    public boolean  validarCartao(CartaoDTO cartao) {
        return true;
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
