package com.qualitrack.production.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qualitrack.production.entite.StatutLot;
import jakarta.validation.constraints.NotNull;

public final class StatutLotUpdateDto {
    @NotNull
    private final StatutLot statut;

    @JsonCreator
    public StatutLotUpdateDto(@JsonProperty("statut") StatutLot statut) {
        this.statut = statut;
    }

    public StatutLot getStatut() {
        return statut;
    }
}

