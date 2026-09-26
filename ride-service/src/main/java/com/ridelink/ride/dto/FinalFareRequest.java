package com.ridelink.ride.dto;

public class FinalFareRequest {
    private Long rideId;
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
