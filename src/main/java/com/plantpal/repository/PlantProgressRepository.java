package com.plantpal.repository;

import com.plantpal.model.PlantProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlantProgressRepository extends JpaRepository<PlantProgress, Long> {
    List<PlantProgress> findByPlantIdOrderByLogDateDesc(Long plantId);
}
