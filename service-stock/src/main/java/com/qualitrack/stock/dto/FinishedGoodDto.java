package com.qualitrack.stock.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.Date;
import java.util.Objects;

public final class FinishedGoodDto {
    private final Long id;
    @NotNull private final Long lotId;
    @NotBlank private final String designation;
    @PositiveOrZero private final int quantite;
    @NotNull private final Date dateEntree;

    @JsonCreator
    public FinishedGoodDto(@JsonProperty("id") Long id,
                          @JsonProperty("lotId") Long lotId,
                          @JsonProperty("designation") String designation,
                          @JsonProperty("quantite") int quantite,
                          @JsonProperty("dateEntree") Date dateEntree) {
        this.id = id;
        this.lotId = lotId;
        this.designation = designation;
        this.quantite = quantite;
        this.dateEntree = dateEntree;
    }

    public Long getId() {
        return id;
    }

    public Long getLotId() {
        return lotId;
    }

    public String getDesignation() {
        return designation;
    }

    public int getQuantite() {
        return quantite;
    }

    public Date getDateEntree() {
        return dateEntree;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FinishedGoodDto that)) return false;
        return quantite == that.quantite
                && Objects.equals(id, that.id)
                && Objects.equals(lotId, that.lotId)
                && Objects.equals(designation, that.designation)
                && Objects.equals(dateEntree, that.dateEntree);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lotId, designation, quantite, dateEntree);
    }
}

