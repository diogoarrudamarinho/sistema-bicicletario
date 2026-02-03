package dev.unirio.rentalservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.unirio.rentalservice.entity.Cartao;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {
    
    Optional<Cartao> findByCiclistaId(Long ciclistaId);
}
