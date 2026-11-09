package com.qualitrack.production.entite;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "production_lot")
public class ProductionLot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String idempotencyKey;

    private Long machineId;
    private Long controleurId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateLot;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutLot statut;

    @Column(precision = 19, scale = 2)
    private BigDecimal coutTotal;

    @OneToMany(mappedBy = "lot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductionMaterial> lignes = new ArrayList<>();

    public ProductionLot() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
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

    public Date getDateLot() {
        return dateLot;
    }

    public void setDateLot(Date dateLot) {
        this.dateLot = dateLot;
    }

    public StatutLot getStatut() {
        return statut;
    }

    public void setStatut(StatutLot statut) {
        this.statut = statut;
    }

    public BigDecimal getCoutTotal() {
        return coutTotal;
    }

    public void setCoutTotal(BigDecimal coutTotal) {
        this.coutTotal = coutTotal;
    }

    public List<ProductionMaterial> getLignes() {
        return lignes;
    }

    public void setLignes(List<ProductionMaterial> lignes) {
        this.lignes = lignes;
    }
}

