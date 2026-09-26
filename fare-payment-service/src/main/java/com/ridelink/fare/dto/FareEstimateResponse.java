package com.ridelink.fare.dto;

public class FareEstimateResponse {

    private Double baseFare;
    private Double ratePerKm;
    private Double distanceKm;
    private Double estimatedFare;
    private String formula;

    public FareEstimateResponse() {
    }

    public FareEstimateResponse(Double baseFare, Double ratePerKm, Double distanceKm, Double estimatedFare, String formula) {
        this.baseFare = baseFare;
        this.ratePerKm = ratePerKm;
        this.distanceKm = distanceKm;
        this.estimatedFare = estimatedFare;
        this.formula = formula;
    }

    public Double getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(Double baseFare) {
        this.baseFare = baseFare;
    }

    public Double getRatePerKm() {
        return ratePerKm;
    }

    public void setRatePerKm(Double ratePerKm) {
        this.ratePerKm = ratePerKm;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public Double getEstimatedFare() {
        return estimatedFare;
    }

    public void setEstimatedFare(Double estimatedFare) {
        this.estimatedFare = estimatedFare;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }
}
