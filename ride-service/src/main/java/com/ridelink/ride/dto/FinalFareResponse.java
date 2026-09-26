package com.ridelink.ride.dto;

public class FinalFareResponse {
    private Long rideId;
    private Double finalFare;
    private String calculationBreakdown;

    public FinalFareResponse() {
    }

    public FinalFareResponse(Long rideId, Double finalFare, String calculationBreakdown) {
        this.rideId = rideId;
        this.finalFare = finalFare;
        this.calculationBreakdown = calculationBreakdown;
    }

    public Long getRideId() {
        return rideId;
    }

    public void setRideId(Long rideId) {
        this.rideId = rideId;
    }

    public Double getFinalFare() {
        return finalFare;
    }

    public void setFinalFare(Double finalFare) {
        this.finalFare = finalFare;
    }

    public String getCalculationBreakdown() {
        return calculationBreakdown;
    }

    public void setCalculationBreakdown(String calculationBreakdown) {
        this.calculationBreakdown = calculationBreakdown;
    }
}
