package dev.unirio.rentalservice.dto;

import jakarta.validation.constraints.NotBlank;

public record FuncionarioDTO(
    Long id,
    
    @NotBlank(message = "Senha é obrigatório")
    String senha,

    @NotBlank(message = "Confirmação de senha é obrigatório")
    String confirmacaoSenha,

    @NotBlank(message = "Email é obrigatório")
    String email,

    @NotBlank(message = "Nome é obrigatório")
    String nome,

    String idade,
    String funcao,

    @NotBlank(message = "CPF é obrigatório")
    String cpf
) {}
