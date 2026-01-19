package dev.unirio.rentalservice.service;

import dev.unirio.rentalservice.dto.CartaoDTO;

public interface CartaoService {
    CartaoDTO buscarCartao(Long ciclista);
    CartaoDTO atualizarCartao(CartaoDTO dto);
}
