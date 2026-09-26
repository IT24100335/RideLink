package com.ridelink.ride.dto;

public class FareEstimateRequest {
    private String pickup;
    private String destination;
    private Double distanceKm;

    public FareEstimateRequest() {
    }

    public FareEstimateRequest(String pickup, String destination, Double distanceKm) {
        this.pickup = pickup;
        this.destination = destination;
        this.distanceKm = distanceKm;
    }

    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }
}
