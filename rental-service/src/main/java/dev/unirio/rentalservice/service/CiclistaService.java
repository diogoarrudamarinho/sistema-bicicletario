package dev.unirio.rentalservice.service;

import dev.unirio.rentalservice.dto.BicicletaDTO;
import dev.unirio.rentalservice.dto.CiclistaDTO;
import dev.unirio.rentalservice.dto.CiclistaRequestDTO;

public interface CiclistaService {
    
    /* GET */
    CiclistaDTO buscarCiclista(Long id);
    BicicletaDTO buscarBicicleta(Long id);
    Boolean permiteAluguel(Long id);
    Boolean emailExistente(String email);

    /* POST */
    CiclistaDTO criarCiclista(CiclistaRequestDTO dto); // tem que mandar email com id do ciclista e um token
    CiclistaDTO ativarCadastro(Long id, String token);

    /* PUT */
    CiclistaDTO atualizarCiclista(Long id, CiclistaRequestDTO dto);

    /* DELETE */
    void desativarCadastro(Long id);

}
