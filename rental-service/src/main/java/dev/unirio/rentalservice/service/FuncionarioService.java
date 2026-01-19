package dev.unirio.rentalservice.service;

import java.util.List;

import dev.unirio.rentalservice.dto.FuncionarioDTO;

public interface FuncionarioService {

    /* GET */
    FuncionarioDTO buscarFuncionario(Long id);
    List<FuncionarioDTO> buscarFuncionarios();

    /* POST */
    FuncionarioDTO criarFuncionario(FuncionarioDTO dto); 

    /* PUT */
    FuncionarioDTO atualizarCiclista(Long id, FuncionarioDTO dto);

    /* DELETE */
    void desativarCadastro(Long id);
}
