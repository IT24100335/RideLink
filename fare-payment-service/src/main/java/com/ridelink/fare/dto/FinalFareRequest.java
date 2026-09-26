package com.ridelink.fare.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class FinalFareRequest {

    @NotNull(message = "Ride ID is required")
    private Long rideId;

    @NotNull(message = "Final distance is required")
    @Positive(message = "Final distance must be greater than zero")
    private Double finalDistanceKm;

    public FinalFareRequest() {
    }

    public FinalFareRequest(Long rideId, Double finalDistanceKm) {
        this.rideId = rideId;
        this.finalDistanceKm = finalDistanceKm;
    }

    public Long getRideId() {
        return rideId;
    }

    public void setRideId(Long rideId) {
        this.rideId = rideId;
    }

    public Double getFinalDistanceKm() {
        return finalDistanceKm;
    }

    public void setFinalDistanceKm(Double finalDistanceKm) {
        this.finalDistanceKm = finalDistanceKm;
    }
}
