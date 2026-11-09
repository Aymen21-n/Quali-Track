package com.qualitrack.production.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ProductionLotCreateLineRequest {
    @NotNull
    private Long rawMaterialId;

    @NotNull
    @Positive
    private Integer quantite;

    public ProductionLotCreateLineRequest() {
    }

    public Long getRawMaterialId() {
        return rawMaterialId;
    }

    public void setRawMaterialId(Long rawMaterialId) {
        this.rawMaterialId = rawMaterialId;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }
}

