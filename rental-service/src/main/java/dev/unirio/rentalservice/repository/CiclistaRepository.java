package dev.unirio.rentalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.unirio.rentalservice.entity.Ciclista;

public interface CiclistaRepository extends JpaRepository<Ciclista, Long>{
    boolean existsByEmail(String email);
}
