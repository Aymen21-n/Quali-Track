package com.qualitrack.production.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qualitrack.production.entite.StatutLot;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public final class ProductionLotDto {
    private final Long id;
    @NotBlank @Size(max = 255) private final String idempotencyKey;
    @NotNull private final Long machineId;
    @NotNull private final Long controleurId;
    @NotNull private final Date dateLot;
    @NotNull private final StatutLot statut;
    private final BigDecimal coutTotal;
    private final List<ProductionMaterialDto> lignes;

    @JsonCreator
    public ProductionLotDto(@JsonProperty("id") Long id,
                           @JsonProperty("idempotencyKey") String idempotencyKey,
                           @JsonProperty("machineId") Long machineId,
                           @JsonProperty("controleurId") Long controleurId,
                           @JsonProperty("dateLot") Date dateLot,
                           @JsonProperty("statut") StatutLot statut,
                           @JsonProperty("coutTotal") BigDecimal coutTotal,
                           @JsonProperty("lignes") List<ProductionMaterialDto> lignes) {
        this.id = id;
        this.idempotencyKey = idempotencyKey;
        this.machineId = machineId;
        this.controleurId = controleurId;
        this.dateLot = dateLot;
        this.statut = statut;
        this.coutTotal = coutTotal;
        this.lignes = lignes;
    }

    public Long getId() {
        return id;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public Long getMachineId() {
        return machineId;
    }

    public Long getControleurId() {
        return controleurId;
    }

    public Date getDateLot() {
        return dateLot;
    }

    public StatutLot getStatut() {
        return statut;
    }

    public BigDecimal getCoutTotal() {
        return coutTotal;
    }

    public List<ProductionMaterialDto> getLignes() {
        return lignes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductionLotDto that)) return false;
        return Objects.equals(id, that.id)
                && Objects.equals(idempotencyKey, that.idempotencyKey)
                && Objects.equals(machineId, that.machineId)
                && Objects.equals(controleurId, that.controleurId)
                && Objects.equals(dateLot, that.dateLot)
                && statut == that.statut
                && Objects.equals(coutTotal, that.coutTotal)
                && Objects.equals(lignes, that.lignes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idempotencyKey, machineId, controleurId, dateLot, statut, coutTotal, lignes);
    }
}

