package com.qualitrack.atelier.entite;

import jakarta.persistence.*;

@Entity
@Table(name = "raw_material")
public class RawMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String fournisseur;
    private double stock;
    private String unite;

    public RawMaterial() {
    }

    public RawMaterial(Long id, String nom, String fournisseur, double stock, String unite) {
        this.id = id;
        this.nom = nom;
        this.fournisseur = fournisseur;
        this.stock = stock;
        this.unite = unite;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getFournisseur() { return fournisseur; }
    public void setFournisseur(String fournisseur) { this.fournisseur = fournisseur; }
    public double getStock() { return stock; }
    public void setStock(double stock) { this.stock = stock; }
    public String getUnite() { return unite; }
    public void setUnite(String unite) { this.unite = unite; }
}
