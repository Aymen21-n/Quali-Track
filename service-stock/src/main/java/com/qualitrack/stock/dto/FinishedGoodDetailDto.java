package com.qualitrack.stock.dto;

import java.util.Date;

public class FinishedGoodDetailDto {
    private Long id;
    private Long lotId;
    private String lotStatut;
    private String designation;
    private int quantite;
    private Date dateEntree;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getLotId() { return lotId; }
    public void setLotId(Long lotId) { this.lotId = lotId; }
    public String getLotStatut() { return lotStatut; }
    public void setLotStatut(String lotStatut) { this.lotStatut = lotStatut; }
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public Date getDateEntree() { return dateEntree; }
    public void setDateEntree(Date dateEntree) { this.dateEntree = dateEntree; }
}

