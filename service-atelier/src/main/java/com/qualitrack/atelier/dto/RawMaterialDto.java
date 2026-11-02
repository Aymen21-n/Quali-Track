package com.qualitrack.atelier.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.util.Objects;

public final class RawMaterialDto {
    private final Long id;
    @NotBlank @Size(max = 100) private final String nom;
    @NotBlank @Size(max = 100) private final String fournisseur;
    @NotNull @PositiveOrZero private final Double stock;
    @NotBlank @Size(max = 20) private final String unite;

    @JsonCreator
    public RawMaterialDto(@JsonProperty("id") Long id, @JsonProperty("nom") String nom,
                          @JsonProperty("fournisseur") String fournisseur, @JsonProperty("stock") Double stock,
                          @JsonProperty("unite") String unite) {
        this.id = id;
        this.nom = nom;
        this.fournisseur = fournisseur;
        this.stock = stock;
        this.unite = unite;
    }

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public String getFournisseur() { return fournisseur; }
    public Double getStock() { return stock; }
    public String getUnite() { return unite; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RawMaterialDto that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(nom, that.nom)
                && Objects.equals(fournisseur, that.fournisseur) && Objects.equals(stock, that.stock)
                && Objects.equals(unite, that.unite);
    }
    @Override public int hashCode() { return Objects.hash(id, nom, fournisseur, stock, unite); }
    @Override public String toString() {
        return "RawMaterialDto{id=" + id + ", nom='" + nom + "', fournisseur='" + fournisseur
                + "', stock=" + stock + ", unite='" + unite + "'}";
    }
}
