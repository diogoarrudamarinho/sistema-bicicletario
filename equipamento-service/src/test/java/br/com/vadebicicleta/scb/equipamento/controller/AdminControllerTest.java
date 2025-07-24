package br.com.vadebicicleta.scb.equipamento.controller;

import br.com.vadebicicleta.scb.equipamento.service.BicicletaService;
import br.com.vadebicicleta.scb.equipamento.service.TotemService;
import br.com.vadebicicleta.scb.equipamento.service.TrancaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BicicletaService bicicletaService;

    @MockBean
    private TrancaService trancaService;

    @MockBean
    private TotemService totemService;

    @Test
    @DisplayName("Deve restaurar o banco chamando os serviços na ordem correta")
    void restaurarBanco_DeveRetornarOkEChamarServicosEmOrdem() throws Exception {
        // ARRANGE
        // Define que os métodos não farão nada quando chamados (pois retornam void)
        doNothing().when(trancaService).restaurarBanco();
        doNothing().when(bicicletaService).restaurarBanco();
        doNothing().when(totemService).restaurarBanco();

        // ACT & ASSERT
        mockMvc.perform(get("/restaurarBanco"))
                .andExpect(status().isOk());

        // VERIFY ORDER
        // Cria um objeto InOrder para verificar a sequência das chamadas
        InOrder inOrder = inOrder(trancaService, bicicletaService, totemService);

        // Verifica se os métodos foram chamados na ordem exata definida
        inOrder.verify(trancaService).restaurarBanco();
        inOrder.verify(bicicletaService).restaurarBanco();
        inOrder.verify(totemService).restaurarBanco();
    }
}
