package dev.unirio.equipmentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.unirio.equipmentservice.entity.Tranca;

@Repository
public interface TrancaRepository extends JpaRepository<Tranca, Long> {
    
}
