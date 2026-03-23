package com.plantpal.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "watering_log")
public class WateringLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;

    @Column(name = "watered_date", nullable = false)
    private LocalDate wateredDate;

    @Column(length = 255)
    private String notes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public WateringLog() {}

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (wateredDate == null) wateredDate = LocalDate.now();
    }

    public Long getId() { return id; }
    // Frontend only needs wateredDate + notes; avoid serializing lazy `plant`.
    @JsonIgnore
    public Plant getPlant() { return plant; }
    public void setPlant(Plant plant) { this.plant = plant; }
    public LocalDate getWateredDate() { return wateredDate; }
    public void setWateredDate(LocalDate wateredDate) { this.wateredDate = wateredDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public static WateringLogBuilder builder() { return new WateringLogBuilder(); }

    public static class WateringLogBuilder {
        private Plant plant;
        private LocalDate wateredDate;
        private String notes;

        public WateringLogBuilder plant(Plant p) { this.plant = p; return this; }
        public WateringLogBuilder wateredDate(LocalDate d) { this.wateredDate = d; return this; }
        public WateringLogBuilder notes(String n) { this.notes = n; return this; }

        public WateringLog build() {
            WateringLog w = new WateringLog();
            w.plant = this.plant;
            w.wateredDate = this.wateredDate;
            w.notes = this.notes;
            return w;
        }
    }
}
