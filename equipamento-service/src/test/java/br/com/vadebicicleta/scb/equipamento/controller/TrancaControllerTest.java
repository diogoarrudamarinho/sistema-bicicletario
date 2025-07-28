package br.com.vadebicicleta.scb.equipamento.controller;

import br.com.vadebicicleta.scb.equipamento.dto.*;
import br.com.vadebicicleta.scb.equipamento.exception.RecursoNaoEncontradoException;
import br.com.vadebicicleta.scb.equipamento.exception.RegraDeNegocioException;
import br.com.vadebicicleta.scb.equipamento.service.TrancaService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrancaController.class)
public class TrancaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TrancaService trancaService;

    private TrancaDTO trancaDTO;
    private NovaTrancaDTO novaTrancaDTO;
    private AlteraTrancaDTO alteraTrancaDTO;
    private TrancaIntegrarDTO trancaIntegrarDTO;
    private TrancaRetirarDTO trancaRetirarDTO;
    private BicicletaDTO bicicletaDTO;
    private Long idValido;
    private Long idInvalido;

    @BeforeEach
    void setUp() {
        idValido = 1l;
        idInvalido = 2l;

        trancaDTO = new TrancaDTO();
        trancaDTO.setId(idValido);
        trancaDTO.setModelo("Modelo X");

        novaTrancaDTO = new NovaTrancaDTO();
        novaTrancaDTO.setModelo("Modelo X");
        novaTrancaDTO.setNumero("001");
        novaTrancaDTO.setAnoDeFabricacao(2023);

        alteraTrancaDTO = new AlteraTrancaDTO();
        alteraTrancaDTO.setModelo("Modelo Y");
        alteraTrancaDTO.setNumero("002");
        alteraTrancaDTO.setAnoDeFabricacao(2024);

        trancaIntegrarDTO = new TrancaIntegrarDTO(1l, 1l);
        trancaRetirarDTO = new TrancaRetirarDTO(1l, "EM_REPARO");

        bicicletaDTO = new BicicletaDTO();
        bicicletaDTO.setId(1l);
    }

    @Test
    @DisplayName("Listar Trancas: Deve retornar 200 OK")
    void listarTrancas_DeveRetornar200() throws Exception {
        given(trancaService.listarTodas()).willReturn(Collections.singletonList(trancaDTO));
        mockMvc.perform(get("/tranca")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Cadastrar Tranca: Deve retornar 201 CREATED com dados válidos")
    void cadastrarTranca_ComDadosValidos_DeveRetornar201() throws Exception {
        given(trancaService.cadastrarTranca(any(NovaTrancaDTO.class))).willReturn(trancaDTO);
        mockMvc.perform(post("/tranca")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novaTrancaDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Buscar Tranca por ID: Deve retornar 404 NOT FOUND quando ID não existe")
    void buscarPorId_QuandoIdNaoExiste_DeveRetornar404() throws Exception {
        given(trancaService.buscarPorId(idInvalido)).willThrow(new RecursoNaoEncontradoException("Tranca não encontrada"));
        mockMvc.perform(get("/tranca/{idTranca}", idInvalido))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Alterar Tranca: Deve retornar 200 OK com dados válidos")
    void alterarTranca_ComDadosValidos_DeveRetornar200() throws Exception {
        given(trancaService.alterarTranca(eq(idValido), any(AlteraTrancaDTO.class))).willReturn(trancaDTO);
        mockMvc.perform(put("/tranca/{idTranca}", idValido)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alteraTrancaDTO)))
                .andExpect(status().isOk());
    }

    // NOVO TESTE PARA COBRIR O CENÁRIO DE SUCESSO DO DELETE
    @Test
    @DisplayName("Deletar Tranca: Deve retornar 204 NO CONTENT quando a exclusão é bem-sucedida")
    void deletar_QuandoExclusaoBemSucedida_DeveRetornar204() throws Exception {
        doNothing().when(trancaService).deletar(idValido);

        mockMvc.perform(delete("/tranca/{idTranca}", idValido))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deletar Tranca: Deve retornar 422 UNPROCESSABLE ENTITY quando regra de negócio falha")
    void deletar_QuandoRegraDeNegocioFalha_DeveRetornar422() throws Exception {
        doThrow(new RegraDeNegocioException("Tranca ocupada")).when(trancaService).deletar(idValido);
        mockMvc.perform(delete("/tranca/{idTranca}", idValido))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Integrar na Rede: Deve retornar 200 OK com dados válidos")
    void integrarNaRede_ComDadosValidos_DeveRetornar200() throws Exception {
        doNothing().when(trancaService).integrarNaRede(eq(idValido), any(TrancaIntegrarDTO.class));
        mockMvc.perform(post("/tranca/{idTranca}/integrarNaRede", idValido)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trancaIntegrarDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Retirar da Rede: Deve retornar 200 OK com dados válidos")
    void retirarDaRede_ComDadosValidos_DeveRetornar200() throws Exception {
        doNothing().when(trancaService).retirarDaRede(eq(idValido), any(TrancaRetirarDTO.class));
        mockMvc.perform(post("/tranca/{idTranca}/retirarDaRede", idValido)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trancaRetirarDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Obter Bicicleta: Deve retornar 200 OK quando bicicleta existe")
    void obterBicicleta_QuandoBicicletaExiste_DeveRetornar200() throws Exception {
        given(trancaService.obterBicicleta(idValido)).willReturn(bicicletaDTO);
        mockMvc.perform(get("/tranca/{idTranca}/bicicleta", idValido))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Obter Bicicleta: Deve retornar 404 NOT FOUND quando tranca está vazia")
    void obterBicicleta_QuandoTrancaVazia_DeveRetornar404() throws Exception {
        given(trancaService.obterBicicleta(idValido)).willThrow(new RecursoNaoEncontradoException("Bicicleta não encontrada"));
        mockMvc.perform(get("/tranca/{idTranca}/bicicleta", idValido))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Alterar Status: Deve retornar 200 OK com ação válida")
    void alterarStatus_ComAcaoValida_DeveRetornar200() throws Exception {
        given(trancaService.alterarStatus(idValido, "TRANCAR")).willReturn(trancaDTO);
        mockMvc.perform(post("/tranca/{idTranca}/status/{acao}", idValido, "TRANCAR"))
                .andExpect(status().isOk());
    }

    @Test
    void deveTrancarComSucesso() throws Exception {
        TrancaDTO tranca = new TrancaDTO();
        tranca.setId(1L);
        tranca.setStatus("LIVRE");
        Long idBicicleta = 2L;

        when(trancaService.trancar(tranca.getId(), idBicicleta)).thenReturn(tranca);

        mockMvc.perform(post("/tranca/{idTranca}/trancar", tranca.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(idBicicleta)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deveDestrancarComSucesso() throws Exception {
       TrancaDTO tranca = new TrancaDTO();
        tranca.setId(1L);
        tranca.setStatus("OCUPADA");
        Long idBicicleta = 2L;

        when(trancaService.destrancar(tranca.getId())).thenReturn(trancaDTO);

        mockMvc.perform(post("/tranca/{idTranca}/destrancar", tranca.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(idBicicleta))) // mesmo que não use no método
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }
}
