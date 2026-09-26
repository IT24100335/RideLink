package com.ridelink.ride.controller;

import com.ridelink.ride.dto.AssignDriverRequest;
import com.ridelink.ride.dto.CreateRideRequest;
import com.ridelink.ride.dto.RideResponse;
import com.ridelink.ride.service.RideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
@Tag(name = "Ride Lifecycle Management", description = "Endpoints for ride booking, driver assignment, state transitions, and tracking")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping
    @Operation(summary = "Request a new ride (initiates fare estimate from Fare Service)")
    public ResponseEntity<RideResponse> createRide(@Valid @RequestBody CreateRideRequest request) {
        RideResponse response = rideService.createRide(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ride details and current lifecycle status by ride ID")
    public ResponseEntity<RideResponse> getRideById(@PathVariable Long id) {
        RideResponse response = rideService.getRideById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/passenger/{passengerId}")
    @Operation(summary = "Get all rides for a specific passenger")
    public ResponseEntity<List<RideResponse>> getRidesByPassengerId(@PathVariable Long passengerId) {
        List<RideResponse> rides = rideService.getRidesByPassengerId(passengerId);
        return ResponseEntity.ok(rides);
    }

    @GetMapping("/driver/{driverId}")
    @Operation(summary = "Get all rides assigned to a specific driver")
    public ResponseEntity<List<RideResponse>> getRidesByDriverId(@PathVariable Long driverId) {
        List<RideResponse> rides = rideService.getRidesByDriverId(driverId);
        return ResponseEntity.ok(rides);
    }

    @PostMapping("/{id}/assign")
    @Operation(summary = "Assign a driver to a REQUESTED ride (queries Driver Service for available drivers)")
    public ResponseEntity<RideResponse> assignDriver(
            @PathVariable Long id,
            @RequestBody(required = false) AssignDriverRequest request) {
        RideResponse response = rideService.assignDriver(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/accept")
    @Operation(summary = "Driver accepts an ASSIGNED ride (transitions to ACCEPTED)")
    public ResponseEntity<RideResponse> acceptRide(@PathVariable Long id) {
        RideResponse response = rideService.acceptRide(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/start")
    @Operation(summary = "Start an ACCEPTED ride (transitions to IN_PROGRESS)")
    public ResponseEntity<RideResponse> startRide(@PathVariable Long id) {
        RideResponse response = rideService.startRide(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "Complete an IN_PROGRESS ride (requests final fare calculation and marks COMPLETED)")
    public ResponseEntity<RideResponse> completeRide(@PathVariable Long id) {
        RideResponse response = rideService.completeRide(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel a ride before it is in progress")
    public ResponseEntity<RideResponse> cancelRide(@PathVariable Long id) {
        RideResponse response = rideService.cancelRide(id);
        return ResponseEntity.ok(response);
    }
}
