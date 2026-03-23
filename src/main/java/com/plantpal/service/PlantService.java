package com.plantpal.service;

import com.plantpal.model.*;
import com.plantpal.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class PlantService {

    private final PlantRepository plantRepository;
    private final WateringLogRepository wateringLogRepository;
    private final PlantProgressRepository progressRepository;
    private final AuthService authService;

    public PlantService(PlantRepository plantRepository, WateringLogRepository wateringLogRepository,
                        PlantProgressRepository progressRepository, AuthService authService) {
        this.plantRepository = plantRepository;
        this.wateringLogRepository = wateringLogRepository;
        this.progressRepository = progressRepository;
        this.authService = authService;
    }

    private User resolveUser(String email) { return authService.getCurrentUser(email); }

    public List<Plant> getAllPlants(String email) {
        return plantRepository.findByUserId(resolveUser(email).getId());
    }

    public Plant getPlant(Long id, String email) {
        return plantRepository.findByIdAndUserId(id, resolveUser(email).getId())
                .orElseThrow(() -> new RuntimeException("Plant not found"));
    }

    @Transactional
    public Plant createPlant(PlantRequest req, String email) {
        User user = resolveUser(email);
        Plant plant = Plant.builder()
                .user(user)
                .emoji(req.emoji != null ? req.emoji : "🌱")
                .plantName(req.plantName).plantType(req.plantType)
                .wateringFrequency(req.wateringFrequency).sunlight(req.sunlight)
                .location(req.location)
                .healthStatus(req.healthStatus != null ? req.healthStatus : Plant.HealthStatus.excellent)
                .notes(req.notes)
                .dateAdded(req.dateAdded != null ? req.dateAdded : LocalDate.now())
                .build();
        return plantRepository.save(plant);
    }

    @Transactional
    public Plant updatePlant(Long id, PlantRequest req, String email) {
        Plant plant = getPlant(id, email);
        if (req.emoji != null) plant.setEmoji(req.emoji);
        if (req.plantName != null) plant.setPlantName(req.plantName);
        if (req.plantType != null) plant.setPlantType(req.plantType);
        if (req.wateringFrequency != null) plant.setWateringFrequency(req.wateringFrequency);
        if (req.sunlight != null) plant.setSunlight(req.sunlight);
        if (req.location != null) plant.setLocation(req.location);
        if (req.healthStatus != null) plant.setHealthStatus(req.healthStatus);
        if (req.notes != null) plant.setNotes(req.notes);
        return plantRepository.save(plant);
    }

    @Transactional
    public void deletePlant(Long id, String email) {
        plantRepository.delete(getPlant(id, email));
    }

    @Transactional
    public WateringLog logWatering(Long plantId, String notes, String email) {
        Plant plant = getPlant(plantId, email);
        WateringLog log = WateringLog.builder().plant(plant)
                .wateredDate(LocalDate.now()).notes(notes).build();
        if (plant.getHealthStatus() == Plant.HealthStatus.needs) {
            plant.setHealthStatus(Plant.HealthStatus.good);
            plantRepository.save(plant);
        }
        return wateringLogRepository.save(log);
    }

    public List<WateringLog> getWateringLogs(Long plantId, String email) {
        getPlant(plantId, email);
        return wateringLogRepository.findByPlantIdOrderByWateredDateDesc(plantId);
    }

    @Transactional
    public PlantProgress logProgress(Long plantId, ProgressRequest req, String email) {
        Plant plant = getPlant(plantId, email);
        PlantProgress progress = PlantProgress.builder().plant(plant)
                .logDate(LocalDate.now()).heightCm(req.heightCm)
                .healthStatus(req.healthStatus).notes(req.notes).build();
        return progressRepository.save(progress);
    }

    public List<PlantProgress> getProgressLogs(Long plantId, String email) {
        getPlant(plantId, email);
        return progressRepository.findByPlantIdOrderByLogDateDesc(plantId);
    }

    public Map<String, Object> getDashboardStats(String email) {
        Long userId = resolveUser(email).getId();
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPlants", plantRepository.countByUserId(userId));
        stats.put("healthyPlants", plantRepository.countByUserIdAndHealthStatus(userId, Plant.HealthStatus.excellent));
        stats.put("needsWatering", plantRepository.countByUserIdAndHealthStatus(userId, Plant.HealthStatus.needs)
                + plantRepository.countByUserIdAndHealthStatus(userId, Plant.HealthStatus.good));
        return stats;
    }

    public static class PlantRequest {
        public String emoji, plantName, plantType, wateringFrequency, sunlight, location, notes;
        public Plant.HealthStatus healthStatus;
        public LocalDate dateAdded;
    }

    public static class ProgressRequest {
        public BigDecimal heightCm;
        public String healthStatus, notes;
    }
}
