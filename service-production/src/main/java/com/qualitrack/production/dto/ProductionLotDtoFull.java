package com.qualitrack.production.dto;

import com.qualitrack.production.entite.StatutLot;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ProductionLotDtoFull {
    private Long id;
    private String idempotencyKey;
    private Long machineId;
    private String machineNom;
    private String machineReference;
    private Long controleurId;
    private String controleurNom;
    private String controleurEmail;
    private Date dateLot;
    private StatutLot statut;
    private BigDecimal coutTotal;
    private List<ProductionMaterialDtoFull> lignes;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
    public Long getMachineId() { return machineId; }
    public void setMachineId(Long machineId) { this.machineId = machineId; }
    public String getMachineNom() { return machineNom; }
    public void setMachineNom(String machineNom) { this.machineNom = machineNom; }
    public String getMachineReference() { return machineReference; }
    public void setMachineReference(String machineReference) { this.machineReference = machineReference; }
    public Long getControleurId() { return controleurId; }
    public void setControleurId(Long controleurId) { this.controleurId = controleurId; }
    public String getControleurNom() { return controleurNom; }
    public void setControleurNom(String controleurNom) { this.controleurNom = controleurNom; }
    public String getControleurEmail() { return controleurEmail; }
    public void setControleurEmail(String controleurEmail) { this.controleurEmail = controleurEmail; }
    public Date getDateLot() { return dateLot; }
    public void setDateLot(Date dateLot) { this.dateLot = dateLot; }
    public StatutLot getStatut() { return statut; }
    public void setStatut(StatutLot statut) { this.statut = statut; }
    public BigDecimal getCoutTotal() { return coutTotal; }
    public void setCoutTotal(BigDecimal coutTotal) { this.coutTotal = coutTotal; }
    public List<ProductionMaterialDtoFull> getLignes() { return lignes; }
    public void setLignes(List<ProductionMaterialDtoFull> lignes) { this.lignes = lignes; }
}

