package dev.unirio.rentalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.unirio.rentalservice.entity.TokenConfirmacao;

public interface TokenConfirmacaoRepository extends JpaRepository<TokenConfirmacao, Long>{
    
}
