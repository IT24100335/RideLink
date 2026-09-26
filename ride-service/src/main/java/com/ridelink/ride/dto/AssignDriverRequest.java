package com.ridelink.ride.dto;

public class AssignDriverRequest {

    private Long driverId; // Optional: If provided, assigns this driver. If null, automatically picks first available.

    public AssignDriverRequest() {
    }

    public AssignDriverRequest(Long driverId) {
        this.driverId = driverId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }
}
