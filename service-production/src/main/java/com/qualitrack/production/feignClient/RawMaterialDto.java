package com.qualitrack.production.feignClient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RawMaterialDto {
    private Long id;
    private String nom;
    private String fournisseur;
    private Double stock;
    private String unite;

    public RawMaterialDto() {
    }

    @JsonCreator
    public RawMaterialDto(@JsonProperty("id") Long id,
                          @JsonProperty("nom") String nom,
                          @JsonProperty("fournisseur") String fournisseur,
                          @JsonProperty("stock") Double stock,
                          @JsonProperty("unite") String unite) {
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
    public Double getStock() { return stock; }
    public void setStock(Double stock) { this.stock = stock; }
    public String getUnite() { return unite; }
    public void setUnite(String unite) { this.unite = unite; }
}

