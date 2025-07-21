package unirio.pm.external_service.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

import unirio.pm.external_service.repository.CobrancaRepository;
import unirio.pm.external_service.repository.FilaCobrancaRepository;
import unirio.pm.external_service.services.implamentation.AdminServiceImplementation;

@ExtendWith(MockitoExtension.class)
class AdminSerivceTest {

    @Mock
    private CobrancaRepository cobrancaRepository;

    @Mock
    private FilaCobrancaRepository filaCobrancaRepository;

    @InjectMocks
    private AdminServiceImplementation service;

    @Test
    void testRestaurarBanco() {
        doNothing().when(cobrancaRepository).deleteAll();
        doNothing().when(filaCobrancaRepository).deleteAll();

        service.restaurarBanco();

        verify(cobrancaRepository).deleteAll();
        verify(filaCobrancaRepository).deleteAll();
    }  
}
