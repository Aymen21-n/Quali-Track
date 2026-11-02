package com.qualitrack.utilisateurs.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.util.Objects;

public final class UserDto {
    private final Long id;
    @NotBlank @Size(max = 100) private final String nom;
    @NotBlank @Email @Size(max = 255) private final String email;
    @NotBlank @Size(max = 50) private final String role;
    @NotNull private final Boolean actif;

    @JsonCreator
    public UserDto(@JsonProperty("id") Long id, @JsonProperty("nom") String nom,
                   @JsonProperty("email") String email, @JsonProperty("role") String role,
                   @JsonProperty("actif") Boolean actif) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.role = role;
        this.actif = actif;
    }

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public Boolean getActif() { return actif; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDto that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(nom, that.nom)
                && Objects.equals(email, that.email) && Objects.equals(role, that.role)
                && Objects.equals(actif, that.actif);
    }
    @Override public int hashCode() { return Objects.hash(id, nom, email, role, actif); }
    @Override public String toString() {
        return "UserDto{id=" + id + ", nom='" + nom + "', email='" + email
                + "', role='" + role + "', actif=" + actif + "}";
    }
}
