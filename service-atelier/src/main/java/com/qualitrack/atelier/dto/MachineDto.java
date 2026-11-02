package com.qualitrack.atelier.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qualitrack.atelier.entite.StatutMachine;
import jakarta.validation.constraints.*;

import java.util.Objects;

public final class MachineDto {
    private final Long id;
    @NotBlank @Size(max = 100) private final String nom;
    @NotBlank @Size(max = 50) private final String type;
    @NotNull @Positive private final Double cadenceHeure;
    @NotNull private final StatutMachine statut;

    @JsonCreator
    public MachineDto(@JsonProperty("id") Long id, @JsonProperty("nom") String nom,
                      @JsonProperty("type") String type, @JsonProperty("cadenceHeure") Double cadenceHeure,
                      @JsonProperty("statut") StatutMachine statut) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.cadenceHeure = cadenceHeure;
        this.statut = statut;
    }

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public String getType() { return type; }
    public Double getCadenceHeure() { return cadenceHeure; }
    public StatutMachine getStatut() { return statut; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MachineDto that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(nom, that.nom)
                && Objects.equals(type, that.type) && Objects.equals(cadenceHeure, that.cadenceHeure)
                && statut == that.statut;
    }
    @Override public int hashCode() { return Objects.hash(id, nom, type, cadenceHeure, statut); }
    @Override public String toString() {
        return "MachineDto{id=" + id + ", nom='" + nom + "', type='" + type
                + "', cadenceHeure=" + cadenceHeure + ", statut=" + statut + "}";
    }
}
