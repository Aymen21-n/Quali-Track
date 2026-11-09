package com.qualitrack.stock.feignClient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class ProductionLotRequest {
    private Long id;
    private String idempotencyKey;
    private Long machineId;
    private Long controleurId;
    private Date dateLot;
    private String statut;

    public ProductionLotRequest() {
    }

    @JsonCreator
    public ProductionLotRequest(@JsonProperty("id") Long id,
                               @JsonProperty("idempotencyKey") String idempotencyKey,
                               @JsonProperty("machineId") Long machineId,
                               @JsonProperty("controleurId") Long controleurId,
                               @JsonProperty("dateLot") Date dateLot,
                               @JsonProperty("statut") String statut) {
        this.id = id;
        this.idempotencyKey = idempotencyKey;
        this.machineId = machineId;
        this.controleurId = controleurId;
        this.dateLot = dateLot;
        this.statut = statut;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
    public Long getMachineId() { return machineId; }
    public void setMachineId(Long machineId) { this.machineId = machineId; }
    public Long getControleurId() { return controleurId; }
    public void setControleurId(Long controleurId) { this.controleurId = controleurId; }
    public Date getDateLot() { return dateLot; }
    public void setDateLot(Date dateLot) { this.dateLot = dateLot; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}

