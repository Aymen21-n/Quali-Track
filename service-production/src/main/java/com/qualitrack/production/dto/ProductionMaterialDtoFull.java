package com.qualitrack.production.dto;

import java.math.BigDecimal;

public class ProductionMaterialDtoFull {
    private Long id;
    private Long rawMaterialId;
    private String rawMaterialNom;
    private String rawMaterialUnite;
    private int quantite;
    private BigDecimal prixUnitaire;
    private BigDecimal sousTotal;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRawMaterialId() { return rawMaterialId; }
    public void setRawMaterialId(Long rawMaterialId) { this.rawMaterialId = rawMaterialId; }
    public String getRawMaterialNom() { return rawMaterialNom; }
    public void setRawMaterialNom(String rawMaterialNom) { this.rawMaterialNom = rawMaterialNom; }
    public String getRawMaterialUnite() { return rawMaterialUnite; }
    public void setRawMaterialUnite(String rawMaterialUnite) { this.rawMaterialUnite = rawMaterialUnite; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public BigDecimal getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(BigDecimal prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    public BigDecimal getSousTotal() { return sousTotal; }
    public void setSousTotal(BigDecimal sousTotal) { this.sousTotal = sousTotal; }
}

