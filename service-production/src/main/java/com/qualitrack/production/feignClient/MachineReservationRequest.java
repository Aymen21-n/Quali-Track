package com.qualitrack.production.feignClient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MachineReservationRequest {
    private String statut;

    public MachineReservationRequest() {
    }

    @JsonCreator
    public MachineReservationRequest(@JsonProperty("statut") String statut) {
        this.statut = statut;
    }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}

