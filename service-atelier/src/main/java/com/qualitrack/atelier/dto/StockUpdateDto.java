package com.qualitrack.atelier.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StockUpdateDto {
    private Integer quantite;

    public StockUpdateDto() {
    }

    @JsonCreator
    public StockUpdateDto(@JsonProperty("quantite") Integer quantite) {
        this.quantite = quantite;
    }

    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }
}

