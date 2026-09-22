package com.ridelink.fare.service;

import com.ridelink.fare.dto.FareEstimateRequest;
import com.ridelink.fare.dto.FareEstimateResponse;
import com.ridelink.fare.dto.FinalFareRequest;
import com.ridelink.fare.dto.FinalFareResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FareService {

    @Value("${fare.base-fare:150.0}")
    private double baseFare;

    @Value("${fare.rate-per-km:100.0}")
    private double ratePerKm;

    public FareEstimateResponse calculateEstimate(FareEstimateRequest request) {
        if (request.getDistanceKm() == null || request.getDistanceKm() <= 0) {
            throw new IllegalArgumentException("Distance must be strictly greater than 0 km.");
        }

        double total = calculateFare(request.getDistanceKm());
        String formula = String.format("Rs. %.2f (Base) + (%.2f km * Rs. %.2f/km)", baseFare, request.getDistanceKm(), ratePerKm);

        return new FareEstimateResponse(baseFare, ratePerKm, request.getDistanceKm(), total, formula);
    }

    public FinalFareResponse calculateFinalFare(FinalFareRequest request) {
        if (request.getFinalDistanceKm() == null || request.getFinalDistanceKm() <= 0) {
            throw new IllegalArgumentException("Final distance must be strictly greater than 0 km.");
        }

        double total = calculateFare(request.getFinalDistanceKm());
        String breakdown = String.format("Base Fare: Rs. %.2f | Rate: Rs. %.2f/km | Distance: %.2f km | Total: Rs. %.2f",
                baseFare, ratePerKm, request.getFinalDistanceKm(), total);

        return new FinalFareResponse(request.getRideId(), total, breakdown);
    }

    public double calculateFare(double distanceKm) {
        return Math.round((baseFare + (distanceKm * ratePerKm)) * 100.0) / 100.0;
    }

    public double getBaseFare() {
        return baseFare;
    }

    public double getRatePerKm() {
        return ratePerKm;
    }
}
