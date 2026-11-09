package com.qualitrack.production.feignClient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDto {
    private Long id;
    private String nom;
    private String email;
    private String role;
    private Boolean actif;

    public UserDto() {
    }

    @JsonCreator
    public UserDto(@JsonProperty("id") Long id,
                   @JsonProperty("nom") String nom,
                   @JsonProperty("email") String email,
                   @JsonProperty("role") String role,
                   @JsonProperty("actif") Boolean actif) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.role = role;
        this.actif = actif;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }
}

