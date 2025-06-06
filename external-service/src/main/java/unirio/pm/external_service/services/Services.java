package unirio.pm.external_service.services;

import unirio.pm.external_service.dto.CartaoDTO;
import unirio.pm.external_service.dto.CobrancaDTO;
import unirio.pm.external_service.dto.EmailDTO;

public interface Services{
    String helloWorld();
    void restaurarBanco();
    EmailDTO enviarEmail(EmailDTO email);
    CobrancaDTO criarCobranca(CobrancaDTO cobranca);
    //List<CobrancaDTO> processarCobrancas();
    //List<CobrancaDTO> addCobranca(String status);
    void validarCartao(CartaoDTO cartao);
}
