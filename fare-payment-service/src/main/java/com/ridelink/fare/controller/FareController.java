package com.ridelink.fare.controller;

import com.ridelink.fare.dto.FareEstimateRequest;
import com.ridelink.fare.dto.FareEstimateResponse;
import com.ridelink.fare.dto.FinalFareRequest;
import com.ridelink.fare.dto.FinalFareResponse;
import com.ridelink.fare.service.FareService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fares")
@Tag(name = "Fare Calculation Engine", description = "Endpoints for preliminary estimation and final fare calculation")
public class FareController {

    private final FareService fareService;

    public FareController(FareService fareService) {
        this.fareService = fareService;
    }

    @PostMapping("/estimate")
    @Operation(summary = "Calculate preliminary fare estimate based on pickup, destination, and distance")
    public ResponseEntity<FareEstimateResponse> getFareEstimate(@Valid @RequestBody FareEstimateRequest request) {
        FareEstimateResponse response = fareService.calculateEstimate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/final")
    @Operation(summary = "Calculate final ride fare based on completed trip distance")
    public ResponseEntity<FinalFareResponse> getFinalFare(@Valid @RequestBody FinalFareRequest request) {
        FinalFareResponse response = fareService.calculateFinalFare(request);
        return ResponseEntity.ok(response);
    }
}
