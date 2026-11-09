package com.qualitrack.production.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.Objects;

public final class ProductionMaterialDto {
    private final Long id;
    @NotNull private final Long rawMaterialId;
    @PositiveOrZero private final int quantite;
    private final BigDecimal prixUnitaire;
    private final BigDecimal sousTotal;

    @JsonCreator
    public ProductionMaterialDto(@JsonProperty("id") Long id,
                                @JsonProperty("rawMaterialId") Long rawMaterialId,
                                @JsonProperty("quantite") int quantite,
                                @JsonProperty("prixUnitaire") BigDecimal prixUnitaire,
                                @JsonProperty("sousTotal") BigDecimal sousTotal) {
        this.id = id;
        this.rawMaterialId = rawMaterialId;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.sousTotal = sousTotal;
    }

    public Long getId() {
        return id;
    }

    public Long getRawMaterialId() {
        return rawMaterialId;
    }

    public int getQuantite() {
        return quantite;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public BigDecimal getSousTotal() {
        return sousTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductionMaterialDto that)) return false;
        return quantite == that.quantite
                && Objects.equals(id, that.id)
                && Objects.equals(rawMaterialId, that.rawMaterialId)
                && Objects.equals(prixUnitaire, that.prixUnitaire)
                && Objects.equals(sousTotal, that.sousTotal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rawMaterialId, quantite, prixUnitaire, sousTotal);
    }
}

