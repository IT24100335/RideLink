package com.ridelink.fare.dto;

import com.ridelink.fare.entity.Receipt;

import java.time.LocalDateTime;

public class ReceiptResponse {

    private Long id;
    private String receiptNumber;
    private Long paymentId;
    private Long rideId;
    private Long passengerId;
    private Double totalAmount;
    private String calculationBreakdown;
    private LocalDateTime issuedAt;

    public ReceiptResponse() {
    }

    public ReceiptResponse(Receipt receipt) {
        this.id = receipt.getId();
        this.receiptNumber = receipt.getReceiptNumber();
        this.paymentId = receipt.getPayment() != null ? receipt.getPayment().getId() : null;
        this.rideId = receipt.getRideId();
        this.passengerId = receipt.getPassengerId();
        this.totalAmount = receipt.getTotalAmount();
        this.calculationBreakdown = receipt.getCalculationBreakdown();
        this.issuedAt = receipt.getIssuedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getRideId() {
        return rideId;
    }

    public void setRideId(Long rideId) {
        this.rideId = rideId;
    }

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCalculationBreakdown() {
        return calculationBreakdown;
    }

    public void setCalculationBreakdown(String calculationBreakdown) {
        this.calculationBreakdown = calculationBreakdown;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }
}
