package dev.unirio.equipmentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.unirio.equipmentservice.entity.Totem;

public interface TotemRepository extends JpaRepository<Totem, Long> {
    
}
