package com.qualitrack.production.entite;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "quality_check")
public class QualityCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id", nullable = false)
    private ProductionLot lot;

    private Long controleurId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateControle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResultatControle resultat;

    @Column(length = 1000)
    private String commentaire;

    public QualityCheck() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductionLot getLot() {
        return lot;
    }

    public void setLot(ProductionLot lot) {
        this.lot = lot;
    }

    public Long getControleurId() {
        return controleurId;
    }

    public void setControleurId(Long controleurId) {
        this.controleurId = controleurId;
    }

    public Date getDateControle() {
        return dateControle;
    }

    public void setDateControle(Date dateControle) {
        this.dateControle = dateControle;
    }

    public ResultatControle getResultat() {
        return resultat;
    }

    public void setResultat(ResultatControle resultat) {
        this.resultat = resultat;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}

