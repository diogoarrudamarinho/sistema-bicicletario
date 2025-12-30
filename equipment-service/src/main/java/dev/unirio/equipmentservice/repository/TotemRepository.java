package dev.unirio.equipmentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.unirio.equipmentservice.entity.Totem;

@Repository
public interface TotemRepository extends JpaRepository<Totem, Long> {
    
}
