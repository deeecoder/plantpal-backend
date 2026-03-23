package com.plantpal.repository;

import com.plantpal.model.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PlantRepository extends JpaRepository<Plant, Long> {
    List<Plant> findByUserId(Long userId);
    Optional<Plant> findByIdAndUserId(Long id, Long userId);
    long countByUserId(Long userId);
    long countByUserIdAndHealthStatus(Long userId, Plant.HealthStatus status);
}
