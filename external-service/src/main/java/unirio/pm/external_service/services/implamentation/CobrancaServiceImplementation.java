package unirio.pm.external_service.services.implamentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import unirio.pm.external_service.client.CartaoClient;
import unirio.pm.external_service.dto.CartaoDTO;
import unirio.pm.external_service.dto.CobrancaDTO;
import unirio.pm.external_service.dto.CobrancaRequestDTO;
import unirio.pm.external_service.entity.Cobranca;
import unirio.pm.external_service.repository.CobrancaRepository;
import unirio.pm.external_service.services.CobrancaService;

@Service
public class CobrancaServiceImplementation implements CobrancaService{
    
    @Autowired
    private CobrancaRepository cobrancaRepository;

    private CartaoClient client;

    @Override
    public CobrancaDTO criarCobranca(CobrancaRequestDTO cobranca){

        if(!validarCartao(client.buscarCartao(cobranca.getCiclista()))) {
            throw new RuntimeException("Erro no Pagamento");
        }
        
        Cobranca entity = new Cobranca(
            cobranca.getValor(),
            cobranca.getCiclista()
        );

        return toDTO(cobrancaRepository.save(entity));
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
