package com.plantpal.controller;

import com.plantpal.model.*;
import com.plantpal.service.PlantService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/plants")
public class PlantController {

    private final PlantService plantService;

    public PlantController(PlantService plantService) { this.plantService = plantService; }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(plantService.getDashboardStats(user.getUsername()));
    }

    @GetMapping
    public ResponseEntity<List<Plant>> getAllPlants(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(plantService.getAllPlants(user.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plant> getPlant(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(plantService.getPlant(id, user.getUsername()));
    }

    @PostMapping
    public ResponseEntity<Plant> createPlant(@RequestBody PlantService.PlantRequest req, @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(plantService.createPlant(req, user.getUsername()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Plant> updatePlant(@PathVariable Long id, @RequestBody PlantService.PlantRequest req, @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(plantService.updatePlant(id, req, user.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletePlant(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        plantService.deletePlant(id, user.getUsername());
        return ResponseEntity.ok(Map.of("message", "Plant deleted successfully"));
    }

    @PostMapping("/{id}/water")
    public ResponseEntity<WateringLog> logWatering(@PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body,
            @AuthenticationPrincipal UserDetails user) {
        String notes = body != null ? body.getOrDefault("notes", "") : "";
        return ResponseEntity.ok(plantService.logWatering(id, notes, user.getUsername()));
    }

    @GetMapping("/{id}/water")
    public ResponseEntity<List<WateringLog>> getWateringLogs(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(plantService.getWateringLogs(id, user.getUsername()));
    }

    @PostMapping("/{id}/progress")
    public ResponseEntity<PlantProgress> logProgress(@PathVariable Long id,
            @RequestBody PlantService.ProgressRequest req,
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(plantService.logProgress(id, req, user.getUsername()));
    }

    @GetMapping("/{id}/progress")
    public ResponseEntity<List<PlantProgress>> getProgress(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(plantService.getProgressLogs(id, user.getUsername()));
    }
}
