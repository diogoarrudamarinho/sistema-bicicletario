package dev.unirio.rentalservice.dto;

public record FuncionarioDTO(
    Long id,
    String senha,
    String confirmacaoSenha,
    String email,
    String nome,
    String idade,
    String funcao,
    String cpf
) {}
