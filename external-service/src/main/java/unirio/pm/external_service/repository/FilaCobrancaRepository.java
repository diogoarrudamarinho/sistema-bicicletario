package unirio.pm.external_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import unirio.pm.external_service.entity.FilaCobranca;

public interface FilaCobrancaRepository extends JpaRepository<FilaCobranca, Long>{
    
}
