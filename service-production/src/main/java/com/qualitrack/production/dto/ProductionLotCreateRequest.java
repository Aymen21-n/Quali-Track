package com.qualitrack.production.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ProductionLotCreateRequest {
    @NotNull
    private Long machineId;

    @NotNull
    private Long controleurId;

    @Valid
    @NotNull
    private List<ProductionLotCreateLineRequest> lignes;

    public ProductionLotCreateRequest() {
    }

    public Long getMachineId() {
        return machineId;
    }

    public void setMachineId(Long machineId) {
        this.machineId = machineId;
    }

    public Long getControleurId() {
        return controleurId;
    }

    public void setControleurId(Long controleurId) {
        this.controleurId = controleurId;
    }

    public List<ProductionLotCreateLineRequest> getLignes() {
        return lignes;
    }

    public void setLignes(List<ProductionLotCreateLineRequest> lignes) {
        this.lignes = lignes;
    }
}

