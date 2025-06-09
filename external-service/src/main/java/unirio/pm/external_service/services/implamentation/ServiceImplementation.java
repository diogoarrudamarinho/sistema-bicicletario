package unirio.pm.external_service.services.implamentation;

import org.springframework.stereotype.Service;

import unirio.pm.external_service.dto.CartaoDTO;
import unirio.pm.external_service.dto.CobrancaDTO;
import unirio.pm.external_service.dto.EmailDTO;
import unirio.pm.external_service.services.Services;

@Service
public class ServiceImplementation implements Services {
    
    @Override
    public String helloWorld(){
        return "Hello world";
    }

    @Override
    public void restaurarBanco() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EmailDTO enviarEmail(EmailDTO email) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CobrancaDTO criarCobranca(CobrancaDTO cobranca) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void validarCartao(CartaoDTO cartao) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
