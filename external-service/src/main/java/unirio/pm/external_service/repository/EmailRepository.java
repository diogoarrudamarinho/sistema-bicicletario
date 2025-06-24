package unirio.pm.external_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import unirio.pm.external_service.entity.Email;

public interface EmailRepository extends JpaRepository<Email, Long> {
    
}
