package com.plantpal.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "plant_progress")
public class PlantProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @Column(name = "height_cm", precision = 6, scale = 2)
    private BigDecimal heightCm;

    @Column(name = "health_status", length = 50)
    private String healthStatus;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public PlantProgress() {}

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (logDate == null) logDate = LocalDate.now();
    }

    public Long getId() { return id; }
    // Frontend only needs logDate/heightCm/healthStatus/notes; avoid lazy `plant`.
    @JsonIgnore
    public Plant getPlant() { return plant; }
    public void setPlant(Plant plant) { this.plant = plant; }
    public LocalDate getLogDate() { return logDate; }
    public void setLogDate(LocalDate logDate) { this.logDate = logDate; }
    public BigDecimal getHeightCm() { return heightCm; }
    public void setHeightCm(BigDecimal heightCm) { this.heightCm = heightCm; }
    public String getHealthStatus() { return healthStatus; }
    public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public static PlantProgressBuilder builder() { return new PlantProgressBuilder(); }

    public static class PlantProgressBuilder {
        private Plant plant;
        private LocalDate logDate;
        private BigDecimal heightCm;
        private String healthStatus;
        private String notes;

        public PlantProgressBuilder plant(Plant p) { this.plant = p; return this; }
        public PlantProgressBuilder logDate(LocalDate d) { this.logDate = d; return this; }
        public PlantProgressBuilder heightCm(BigDecimal h) { this.heightCm = h; return this; }
        public PlantProgressBuilder healthStatus(String h) { this.healthStatus = h; return this; }
        public PlantProgressBuilder notes(String n) { this.notes = n; return this; }

        public PlantProgress build() {
            PlantProgress p = new PlantProgress();
            p.plant = this.plant;
            p.logDate = this.logDate;
            p.heightCm = this.heightCm;
            p.healthStatus = this.healthStatus;
            p.notes = this.notes;
            return p;
        }
    }
}
