package dev.unirio.externalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.unirio.externalservice.entity.Cobranca;

public interface CobrancaRepository extends JpaRepository<Cobranca, Long>{
    
}
