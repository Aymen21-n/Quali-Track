package com.qualitrack.production.feignClient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RawMaterialStockUpdateRequest {
    private Integer quantite;

    public RawMaterialStockUpdateRequest() {
    }

    @JsonCreator
    public RawMaterialStockUpdateRequest(@JsonProperty("quantite") Integer quantite) {
        this.quantite = quantite;
    }

    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }
}

