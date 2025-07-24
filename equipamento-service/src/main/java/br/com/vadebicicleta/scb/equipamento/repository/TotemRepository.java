package br.com.vadebicicleta.scb.equipamento.repository;

import br.com.vadebicicleta.scb.equipamento.entity.Totem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TotemRepository extends JpaRepository<Totem, Long> {
    Optional<Totem> findByPublicId(UUID publicId);
}