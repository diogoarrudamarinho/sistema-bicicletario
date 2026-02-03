package dev.unirio.rentalservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.unirio.rentalservice.entity.TokenConfirmacao;


public interface TokenConfirmacaoRepository extends JpaRepository<TokenConfirmacao, Long>{
    Optional<TokenConfirmacao> findByToken(String token);
}
