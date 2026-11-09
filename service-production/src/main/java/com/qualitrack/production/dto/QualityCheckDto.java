package com.qualitrack.production.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qualitrack.production.entite.ResultatControle;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;
import java.util.Objects;

public final class QualityCheckDto {
    private final Long id;
    @NotNull private final Long lotId;
    @NotNull private final Long controleurId;
    @NotNull private final Date dateControle;
    @NotNull private final ResultatControle resultat;
    @Size(max = 1000) private final String commentaire;

    @JsonCreator
    public QualityCheckDto(@JsonProperty("id") Long id,
                          @JsonProperty("lotId") Long lotId,
                          @JsonProperty("controleurId") Long controleurId,
                          @JsonProperty("dateControle") Date dateControle,
                          @JsonProperty("resultat") ResultatControle resultat,
                          @JsonProperty("commentaire") String commentaire) {
        this.id = id;
        this.lotId = lotId;
        this.controleurId = controleurId;
        this.dateControle = dateControle;
        this.resultat = resultat;
        this.commentaire = commentaire;
    }

    public Long getId() {
        return id;
    }

    public Long getLotId() {
        return lotId;
    }

    public Long getControleurId() {
        return controleurId;
    }

    public Date getDateControle() {
        return dateControle;
    }

    public ResultatControle getResultat() {
        return resultat;
    }

    public String getCommentaire() {
        return commentaire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QualityCheckDto that)) return false;
        return Objects.equals(id, that.id)
                && Objects.equals(lotId, that.lotId)
                && Objects.equals(controleurId, that.controleurId)
                && Objects.equals(dateControle, that.dateControle)
                && resultat == that.resultat
                && Objects.equals(commentaire, that.commentaire);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lotId, controleurId, dateControle, resultat, commentaire);
    }
}

