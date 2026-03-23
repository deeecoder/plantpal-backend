package com.plantpal.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "plants")
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 10)
    private String emoji;

    @Column(name = "plant_name", nullable = false, length = 100)
    private String plantName;

    @Column(name = "plant_type", nullable = false, length = 50)
    private String plantType;

    @Column(name = "watering_frequency", length = 50)
    private String wateringFrequency;

    @Column(length = 50)
    private String sunlight;

    @Column(length = 100)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "health_status")
    private HealthStatus healthStatus = HealthStatus.excellent;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "date_added")
    private LocalDate dateAdded;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WateringLog> wateringLogs;

    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PlantProgress> progressLogs;

    public Plant() {}

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (dateAdded == null) dateAdded = LocalDate.now();
        if (emoji == null) emoji = "🌱";
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum HealthStatus { excellent, good, needs }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    // Prevent lazy-loading Hibernate proxies during JSON serialization.
    @JsonIgnore
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getEmoji() { return emoji; }
    public void setEmoji(String emoji) { this.emoji = emoji; }

    public String getPlantName() { return plantName; }
    public void setPlantName(String plantName) { this.plantName = plantName; }

    public String getPlantType() { return plantType; }
    public void setPlantType(String plantType) { this.plantType = plantType; }

    public String getWateringFrequency() { return wateringFrequency; }
    public void setWateringFrequency(String wateringFrequency) { this.wateringFrequency = wateringFrequency; }

    public String getSunlight() { return sunlight; }
    public void setSunlight(String sunlight) { this.sunlight = sunlight; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public HealthStatus getHealthStatus() { return healthStatus; }
    public void setHealthStatus(HealthStatus healthStatus) { this.healthStatus = healthStatus; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDate getDateAdded() { return dateAdded; }
    public void setDateAdded(LocalDate dateAdded) { this.dateAdded = dateAdded; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public static PlantBuilder builder() { return new PlantBuilder(); }

    public static class PlantBuilder {
        private User user;
        private String emoji;
        private String plantName;
        private String plantType;
        private String wateringFrequency;
        private String sunlight;
        private String location;
        private HealthStatus healthStatus;
        private String notes;
        private LocalDate dateAdded;

        public PlantBuilder user(User user) { this.user = user; return this; }
        public PlantBuilder emoji(String emoji) { this.emoji = emoji; return this; }
        public PlantBuilder plantName(String n) { this.plantName = n; return this; }
        public PlantBuilder plantType(String t) { this.plantType = t; return this; }
        public PlantBuilder wateringFrequency(String w) { this.wateringFrequency = w; return this; }
        public PlantBuilder sunlight(String s) { this.sunlight = s; return this; }
        public PlantBuilder location(String l) { this.location = l; return this; }
        public PlantBuilder healthStatus(HealthStatus h) { this.healthStatus = h; return this; }
        public PlantBuilder notes(String n) { this.notes = n; return this; }
        public PlantBuilder dateAdded(LocalDate d) { this.dateAdded = d; return this; }

        public Plant build() {
            Plant p = new Plant();
            p.user = this.user;
            p.emoji = this.emoji;
            p.plantName = this.plantName;
            p.plantType = this.plantType;
            p.wateringFrequency = this.wateringFrequency;
            p.sunlight = this.sunlight;
            p.location = this.location;
            p.healthStatus = this.healthStatus != null ? this.healthStatus : HealthStatus.excellent;
            p.notes = this.notes;
            p.dateAdded = this.dateAdded;
            return p;
        }
    }
}
