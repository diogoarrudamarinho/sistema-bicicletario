package dev.unirio.rentalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.unirio.rentalservice.entity.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    
}
