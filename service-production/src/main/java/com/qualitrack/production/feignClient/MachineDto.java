package com.qualitrack.production.feignClient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MachineDto {
    private Long id;
    private String nom;
    private String type;
    private Double cadenceHeure;
    private String statut;

    public MachineDto() {
    }

    @JsonCreator
    public MachineDto(@JsonProperty("id") Long id,
                     @JsonProperty("nom") String nom,
                     @JsonProperty("type") String type,
                     @JsonProperty("cadenceHeure") Double cadenceHeure,
                     @JsonProperty("statut") String statut) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.cadenceHeure = cadenceHeure;
        this.statut = statut;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Double getCadenceHeure() { return cadenceHeure; }
    public void setCadenceHeure(Double cadenceHeure) { this.cadenceHeure = cadenceHeure; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}

