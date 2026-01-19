package dev.unirio.rentalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.unirio.rentalservice.entity.Cartao;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {
    
}
