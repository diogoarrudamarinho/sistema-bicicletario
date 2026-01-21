package dev.unirio.rentalservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.unirio.rentalservice.entity.Aluguel;

public interface AluguelRepository extends JpaRepository<Aluguel, Long> {
    Optional<Aluguel> findByCiclistaAndHoraFimIsNull(Long ciclista);
    boolean existsByCiclistaAndHoraFimIsNull(Long ciclista);
}
