package com.qualitrack.stock.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.Objects;

public final class DefectDto {
    private final Long id;
    @NotNull private final Long lotId;
    @NotBlank private final String type;
    @NotBlank private final String description;
    @NotBlank private final String gravite;
    @NotNull private final Date dateDetection;

    @JsonCreator
    public DefectDto(@JsonProperty("id") Long id,
                     @JsonProperty("lotId") Long lotId,
                     @JsonProperty("type") String type,
                     @JsonProperty("description") String description,
                     @JsonProperty("gravite") String gravite,
                     @JsonProperty("dateDetection") Date dateDetection) {
        this.id = id;
        this.lotId = lotId;
        this.type = type;
        this.description = description;
        this.gravite = gravite;
        this.dateDetection = dateDetection;
    }

    public Long getId() {
        return id;
    }

    public Long getLotId() {
        return lotId;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getGravite() {
        return gravite;
    }

    public Date getDateDetection() {
        return dateDetection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DefectDto that)) return false;
        return Objects.equals(id, that.id)
                && Objects.equals(lotId, that.lotId)
                && Objects.equals(type, that.type)
                && Objects.equals(description, that.description)
                && Objects.equals(gravite, that.gravite)
                && Objects.equals(dateDetection, that.dateDetection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lotId, type, description, gravite, dateDetection);
    }
}

