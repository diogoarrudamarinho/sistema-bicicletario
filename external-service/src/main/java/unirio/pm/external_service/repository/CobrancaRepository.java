package unirio.pm.external_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import unirio.pm.external_service.entity.Cobranca;

public interface CobrancaRepository extends JpaRepository<Cobranca, Long>{
    
}
