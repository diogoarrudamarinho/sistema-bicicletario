package dev.unirio.rentalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.unirio.rentalservice.entity.Aluguel;

public interface AluguelRepository extends JpaRepository<Aluguel, Long> {
    
}
