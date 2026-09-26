package com.ridelink.ride.dto;

import com.ridelink.ride.entity.Ride;
import com.ridelink.ride.entity.RideStatus;

import java.time.LocalDateTime;

public class RideResponse {

    private Long id;
    private Long passengerId;
    private Long driverId;
    private String pickup;
    private String destination;
    private Double distanceKm;
    private RideStatus status;
    private Double estimatedFare;
    private Double finalFare;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    public RideResponse() {
    }

    public RideResponse(Ride ride) {
        this.id = ride.getId();
        this.passengerId = ride.getPassengerId();
        this.driverId = ride.getDriverId();
        this.pickup = ride.getPickup();
        this.destination = ride.getDestination();
        this.distanceKm = ride.getDistanceKm();
        this.status = ride.getStatus();
        this.estimatedFare = ride.getEstimatedFare();
        this.finalFare = ride.getFinalFare();
        this.createdAt = ride.getCreatedAt();
        this.completedAt = ride.getCompletedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
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

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }

    public Double getEstimatedFare() {
        return estimatedFare;
    }

    public void setEstimatedFare(Double estimatedFare) {
        this.estimatedFare = estimatedFare;
    }

    public Double getFinalFare() {
        return finalFare;
    }

    public void setFinalFare(Double finalFare) {
        this.finalFare = finalFare;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
