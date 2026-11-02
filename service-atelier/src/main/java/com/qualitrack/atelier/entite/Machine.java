package com.qualitrack.atelier.entite;

import jakarta.persistence.*;

@Entity
@Table(name = "machine")
public class Machine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String type;
    private double cadenceHeure;
    @Enumerated(EnumType.STRING)
    private StatutMachine statut;

    public Machine() {
    }

    public Machine(Long id, String nom, String type, double cadenceHeure, StatutMachine statut) {
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
    public double getCadenceHeure() { return cadenceHeure; }
    public void setCadenceHeure(double cadenceHeure) { this.cadenceHeure = cadenceHeure; }
    public StatutMachine getStatut() { return statut; }
    public void setStatut(StatutMachine statut) { this.statut = statut; }
}
