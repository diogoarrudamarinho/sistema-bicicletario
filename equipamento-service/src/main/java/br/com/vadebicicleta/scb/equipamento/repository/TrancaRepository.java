package br.com.vadebicicleta.scb.equipamento.repository;

import br.com.vadebicicleta.scb.equipamento.entity.Tranca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrancaRepository extends JpaRepository<Tranca, Long> {
    Optional<Tranca> findByPublicId(UUID publicId);
}