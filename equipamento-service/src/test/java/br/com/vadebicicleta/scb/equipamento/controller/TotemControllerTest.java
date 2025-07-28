package br.com.vadebicicleta.scb.equipamento.controller;

import br.com.vadebicicleta.scb.equipamento.dto.AlteraTotemDTO;
import br.com.vadebicicleta.scb.equipamento.dto.NovoTotemDTO;
import br.com.vadebicicleta.scb.equipamento.dto.TotemDTO;
import br.com.vadebicicleta.scb.equipamento.exception.RecursoNaoEncontradoException;
import br.com.vadebicicleta.scb.equipamento.exception.RegraDeNegocioException;
import br.com.vadebicicleta.scb.equipamento.service.TotemService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TotemController.class)
public class TotemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TotemService totemService;

    private TotemDTO totemDTO;
    private NovoTotemDTO novoTotemDTO;
    private AlteraTotemDTO alteraTotemDTO;
    private Long idValido;
    private Long idInvalido;

    @BeforeEach
    void setUp() {
        idValido = 1l;
        idInvalido = 2l;

        totemDTO = new TotemDTO();
        totemDTO.setId(idValido);
        totemDTO.setLocalizacao("Praça da Sé");

        novoTotemDTO = new NovoTotemDTO();
        novoTotemDTO.setLocalizacao("Praça da Sé");
        novoTotemDTO.setDescricao("Totem central");

        alteraTotemDTO = new AlteraTotemDTO();
        alteraTotemDTO.setLocalizacao("Parque Ibirapuera");
        alteraTotemDTO.setDescricao("Totem perto do lago");
    }

    @Test
    @DisplayName("Cadastrar Totem: Deve retornar 201 CREATED com dados válidos")
    void cadastrarTotem_ComDadosValidos_DeveRetornar201() throws Exception {
        given(totemService.cadastrarTotem(any(NovoTotemDTO.class))).willReturn(totemDTO);

        mockMvc.perform(post("/totem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoTotemDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(idValido.toString()));
    }

    @Test
    @DisplayName("Listar Totens: Deve retornar 200 OK com a lista de totens")
    void listarTotens_DeveRetornar200() throws Exception {
        given(totemService.listarTodos()).willReturn(Collections.singletonList(totemDTO));

        mockMvc.perform(get("/totem"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(idValido.toString()));
    }

    @Test
    @DisplayName("Buscar Totem por ID: Deve retornar 200 OK quando ID existe")
    void buscarPorId_QuandoIdExiste_DeveRetornar200() throws Exception {
        given(totemService.buscarPorId(idValido)).willReturn(totemDTO);

        mockMvc.perform(get("/totem/{idTotem}", idValido))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Buscar Totem por ID: Deve retornar 404 NOT FOUND quando ID não existe")
    void buscarPorId_QuandoIdNaoExiste_DeveRetornar404() throws Exception {
        given(totemService.buscarPorId(idInvalido)).willThrow(new RecursoNaoEncontradoException("Totem não encontrado"));

        mockMvc.perform(get("/totem/{idTotem}", idInvalido))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Alterar Totem: Deve retornar 200 OK com dados válidos")
    void alterarTotem_ComDadosValidos_DeveRetornar200() throws Exception {
        given(totemService.alterarTotem(eq(idValido), any(AlteraTotemDTO.class))).willReturn(totemDTO);

        mockMvc.perform(put("/totem/{idTotem}", idValido)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alteraTotemDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Listar Trancas do Totem: Deve retornar 200 OK quando ID existe")
    void listarTrancasDoTotem_QuandoIdExiste_DeveRetornar200() throws Exception {
        given(totemService.listarTrancasDoTotem(idValido)).willReturn(Collections.emptyList());

        mockMvc.perform(get("/totem/{idTotem}/trancas", idValido))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Listar Bicicletas do Totem: Deve retornar 200 OK quando ID existe")
    void listarBicicletasDoTotem_QuandoIdExiste_DeveRetornar200() throws Exception {
        given(totemService.listarBicicletasDoTotem(idValido)).willReturn(Collections.emptyList());

        mockMvc.perform(get("/totem/{idTotem}/bicicletas", idValido))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deletar Totem: Deve retornar 204 NO CONTENT quando ID existe e totem está vazio")
    void deletar_QuandoIdValidoETotemVazio_DeveRetornar204() throws Exception {
        doNothing().when(totemService).deletar(idValido);

        mockMvc.perform(delete("/totem/{idTotem}", idValido))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deletar Totem: Deve retornar 422 UNPROCESSABLE ENTITY quando totem tem trancas")
    void deletar_QuandoTotemPossuiTrancas_DeveRetornar422() throws Exception {
        doThrow(new RegraDeNegocioException("Não é possível remover totem com trancas")).when(totemService).deletar(idValido);

        mockMvc.perform(delete("/totem/{idTotem}", idValido))
                .andExpect(status().isUnprocessableEntity());
    }
}
