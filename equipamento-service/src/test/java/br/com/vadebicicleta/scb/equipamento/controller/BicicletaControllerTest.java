package br.com.vadebicicleta.scb.equipamento.controller;

import br.com.vadebicicleta.scb.equipamento.dto.*;
import br.com.vadebicicleta.scb.equipamento.exception.RecursoNaoEncontradoException;
import br.com.vadebicicleta.scb.equipamento.exception.RegraDeNegocioException;
import br.com.vadebicicleta.scb.equipamento.service.BicicletaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BicicletaController.class)
public class BicicletaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BicicletaService bicicletaService;

    private BicicletaDTO bicicletaDTO;
    private NovaBicicletaDTO novaBicicletaDTO;
    private AlteraBicicletaDTO alteraBicicletaDTO;
    private IntegrarNaRedeDTO integrarNaRedeDTO;
    private RetirarDaRedeDTO retirarDaRedeDTO;
    private UUID idValido;
    private UUID idInvalido;

    @BeforeEach
    void setUp() {
        idValido = UUID.randomUUID();
        idInvalido = UUID.randomUUID();

        bicicletaDTO = new BicicletaDTO();
        bicicletaDTO.setId(idValido);
        bicicletaDTO.setMarca("Caloi");
        bicicletaDTO.setModelo("10");

        novaBicicletaDTO = new NovaBicicletaDTO();
        novaBicicletaDTO.setMarca("Caloi");
        novaBicicletaDTO.setModelo("10");
        novaBicicletaDTO.setAno(2024);

        alteraBicicletaDTO = new AlteraBicicletaDTO();
        alteraBicicletaDTO.setMarca("Monark");
        alteraBicicletaDTO.setModelo("BMX");
        alteraBicicletaDTO.setAno(2025);

        integrarNaRedeDTO = new IntegrarNaRedeDTO();
        integrarNaRedeDTO.setIdFuncionario(UUID.randomUUID());
        integrarNaRedeDTO.setIdBicicleta(UUID.randomUUID());
        integrarNaRedeDTO.setIdTranca(UUID.randomUUID());

        retirarDaRedeDTO = new RetirarDaRedeDTO();
        retirarDaRedeDTO.setIdFuncionario(UUID.randomUUID());
        retirarDaRedeDTO.setIdBicicleta(UUID.randomUUID());
        retirarDaRedeDTO.setIdTranca(UUID.randomUUID());
        retirarDaRedeDTO.setStatusAcaoReparador("EM_REPARO");
    }

    // Testes para POST /bicicleta
    @Test
    @DisplayName("Cadastrar: Deve retornar 201 CREATED com dados válidos")
    void cadastrarBicicleta_ComDadosValidos_DeveRetornar201() throws Exception {
        given(bicicletaService.cadastrarBicicleta(any(NovaBicicletaDTO.class))).willReturn(bicicletaDTO);

        mockMvc.perform(post("/bicicleta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novaBicicletaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(idValido.toString()));
    }

    // Testes para GET /bicicleta
    @Test
    @DisplayName("Listar: Deve retornar 200 OK com a lista de bicicletas")
    void listarBicicletas_DeveRetornar200() throws Exception {
        List<BicicletaDTO> lista = Collections.singletonList(bicicletaDTO);
        given(bicicletaService.listarTodas()).willReturn(lista);

        mockMvc.perform(get("/bicicleta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(idValido.toString()));
    }

    // Testes para GET /bicicleta/{idBicicleta}
    @Test
    @DisplayName("Buscar por ID: Deve retornar 200 OK quando ID existe")
    void getBicicletaById_QuandoIdExiste_DeveRetornar200() throws Exception {
        given(bicicletaService.buscarPorId(idValido)).willReturn(bicicletaDTO);

        mockMvc.perform(get("/bicicleta/{idBicicleta}", idValido))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idValido.toString()));
    }

    @Test
    @DisplayName("Buscar por ID: Deve retornar 404 NOT FOUND quando ID não existe")
    void getBicicletaById_QuandoIdNaoExiste_DeveRetornar404() throws Exception {
        given(bicicletaService.buscarPorId(idInvalido)).willThrow(new RecursoNaoEncontradoException("Bicicleta não encontrada"));

        mockMvc.perform(get("/bicicleta/{idBicicleta}", idInvalido))
                .andExpect(status().isNotFound());
    }

    // Testes para PUT /bicicleta/{idBicicleta}
    @Test
    @DisplayName("Alterar: Deve retornar 200 OK com dados válidos")
    void alterarBicicleta_ComDadosValidos_DeveRetornar200() throws Exception {
        given(bicicletaService.alterarBicicleta(eq(idValido), any(AlteraBicicletaDTO.class))).willReturn(bicicletaDTO);

        mockMvc.perform(put("/bicicleta/{idBicicleta}", idValido)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alteraBicicletaDTO)))
                .andExpect(status().isOk());
    }

    // Testes para DELETE /bicicleta/{idBicicleta}
    @Test
    @DisplayName("Deletar: Deve retornar 204 NO CONTENT quando ID existe")
    void deletarBicicleta_QuandoIdExiste_DeveRetornar204() throws Exception {
        doNothing().when(bicicletaService).deletar(idValido);

        mockMvc.perform(delete("/bicicleta/{idBicicleta}", idValido))
                .andExpect(status().isNoContent());
    }

    // Testes para POST /bicicleta/integrarNaRede
    @Test
    @DisplayName("Integrar: Deve retornar 200 OK com dados válidos")
    void integrarNaRede_ComDadosValidos_DeveRetornar200() throws Exception {
        doNothing().when(bicicletaService).integrarNaRede(any(IntegrarNaRedeDTO.class));

        mockMvc.perform(post("/bicicleta/integrarNaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(integrarNaRedeDTO)))
                .andExpect(status().isOk());
    }

    // Testes para POST /bicicleta/retirarDaRede
    @Test
    @DisplayName("Retirar: Deve retornar 200 OK com dados válidos")
    void retirarDaRede_ComDadosValidos_DeveRetornar200() throws Exception {
        doNothing().when(bicicletaService).retirarDaRede(any(RetirarDaRedeDTO.class));

        mockMvc.perform(post("/bicicleta/retirarDaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(retirarDaRedeDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Retirar: Deve retornar 422 UNPROCESSABLE ENTITY quando regra de negócio falha")
    void retirarDaRede_QuandoRegraDeNegocioFalha_DeveRetornar422() throws Exception {
        doThrow(new RegraDeNegocioException("Bicicleta em estado inválido")).when(bicicletaService).retirarDaRede(any(RetirarDaRedeDTO.class));

        mockMvc.perform(post("/bicicleta/retirarDaRede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(retirarDaRedeDTO)))
                .andExpect(status().isUnprocessableEntity());
    }

    // Testes para POST /bicicleta/{idBicicleta}/status/{acao}
    @Test
    @DisplayName("Alterar Status: Deve retornar 200 OK com dados válidos")
    void alterarStatus_ComDadosValidos_DeveRetornar200() throws Exception {
        String acao = "APOSENTADA";
        given(bicicletaService.alterarStatus(idValido, acao)).willReturn(bicicletaDTO);

        mockMvc.perform(post("/bicicleta/{idBicicleta}/status/{acao}", idValido, acao))
                .andExpect(status().isOk());
    }
}
