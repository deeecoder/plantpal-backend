package com.plantpal.repository;

import com.plantpal.model.WateringLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WateringLogRepository extends JpaRepository<WateringLog, Long> {
    List<WateringLog> findByPlantIdOrderByWateredDateDesc(Long plantId);
}
