package dev.unirio.externalservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.unirio.externalservice.entity.Cobranca;
import dev.unirio.externalservice.enumeration.StatusCobranca;

public interface CobrancaRepository extends JpaRepository<Cobranca, Long>{
    
    // Busca a cobrança por status
    List<Cobranca> findAllByStatus(StatusCobranca status);
}
