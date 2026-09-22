package com.ridelink.ride.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateRideRequest {

    @NotNull(message = "Passenger ID is required")
    private Long passengerId;

    @NotBlank(message = "Pickup location is required")
    private String pickup;

    @NotBlank(message = "Destination is required")
    private String destination;

    @NotNull(message = "Distance in KM is required")
    @Positive(message = "Distance must be greater than zero")
    private Double distanceKm;

    public CreateRideRequest() {
    }

    public CreateRideRequest(Long passengerId, String pickup, String destination, Double distanceKm) {
        this.passengerId = passengerId;
        this.pickup = pickup;
        this.destination = destination;
        this.distanceKm = distanceKm;
    }

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
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
