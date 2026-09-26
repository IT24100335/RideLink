package com.ridelink.ride.dto;

public class DriverDTO {
    private Long driverId;
    private Long accountId;
    private String licenseNumber;
    private String vehicleType;
    private String vehicleNumber;
    private String serviceArea;
    private String availability;

    public DriverDTO() {
    }

    public DriverDTO(Long driverId, Long accountId, String licenseNumber, String vehicleType,
                     String vehicleNumber, String serviceArea, String availability) {
        this.driverId = driverId;
        this.accountId = accountId;
        this.licenseNumber = licenseNumber;
        this.vehicleType = vehicleType;
        this.vehicleNumber = vehicleNumber;
        this.serviceArea = serviceArea;
        this.availability = availability;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(String serviceArea) {
        this.serviceArea = serviceArea;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
