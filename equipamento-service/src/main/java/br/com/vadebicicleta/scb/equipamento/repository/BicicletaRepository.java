package br.com.vadebicicleta.scb.equipamento.repository;

import br.com.vadebicicleta.scb.equipamento.entity.Bicicleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BicicletaRepository extends JpaRepository<Bicicleta, Long> {
    Optional<Bicicleta> findByPublicId(UUID publicId);
}