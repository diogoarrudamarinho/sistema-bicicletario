package unirio.pm.external_service.services.implamentation;

import org.springframework.stereotype.Service;

import unirio.pm.external_service.repository.CobrancaRepository;
import unirio.pm.external_service.repository.FilaCobrancaRepository;
import unirio.pm.external_service.services.AdminService;

@Service
public class AdminServiceImplementation implements AdminService{
    
     private final CobrancaRepository cobrancaRepository;
    private final FilaCobrancaRepository filaCobrancaRepository;

    public AdminServiceImplementation(
        CobrancaRepository cobrancaRepository,
        FilaCobrancaRepository filaCobrancaRepository
    ) {
        this.cobrancaRepository = cobrancaRepository;
        this.filaCobrancaRepository = filaCobrancaRepository;
    }

    @Override
    public void restaurarBanco() {
        cobrancaRepository.deleteAll();
        filaCobrancaRepository.deleteAll();
    }
}

