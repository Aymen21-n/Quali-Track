package com.qualitrack.stock.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "defect")
public class Defect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long lotId;
    private String type;
    private String description;
    private String gravite;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDetection;

    public Defect() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLotId() {
        return lotId;
    }

    public void setLotId(Long lotId) {
        this.lotId = lotId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGravite() {
        return gravite;
    }

    public void setGravite(String gravite) {
        this.gravite = gravite;
    }

    public Date getDateDetection() {
        return dateDetection;
    }

    public void setDateDetection(Date dateDetection) {
        this.dateDetection = dateDetection;
    }
}

