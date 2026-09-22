package com.ridelink.ride.service;

import com.ridelink.ride.client.DriverServiceClient;
import com.ridelink.ride.client.FareServiceClient;
import com.ridelink.ride.dto.*;
import com.ridelink.ride.entity.Ride;
import com.ridelink.ride.entity.RideStatus;
import com.ridelink.ride.exception.InvalidRideStatusException;
import com.ridelink.ride.exception.NoAvailableDriverException;
import com.ridelink.ride.exception.ResourceNotFoundException;
import com.ridelink.ride.repository.RideRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RideService {

    private final RideRepository rideRepository;
    private final DriverServiceClient driverServiceClient;
    private final FareServiceClient fareServiceClient;

    public RideService(RideRepository rideRepository,
                       DriverServiceClient driverServiceClient,
                       FareServiceClient fareServiceClient) {
        this.rideRepository = rideRepository;
        this.driverServiceClient = driverServiceClient;
        this.fareServiceClient = fareServiceClient;
    }

    @Transactional
    public RideResponse createRide(CreateRideRequest request) {
        // Interservice interaction: Estimate fare
        FareEstimateResponse fareEstimate = fareServiceClient.getFareEstimate(
                request.getPickup(),
                request.getDestination(),
                request.getDistanceKm()
        );

        Ride ride = new Ride();
        ride.setPassengerId(request.getPassengerId());
        ride.setPickup(request.getPickup().trim());
        ride.setDestination(request.getDestination().trim());
        ride.setDistanceKm(request.getDistanceKm());
        ride.setStatus(RideStatus.REQUESTED);
        ride.setEstimatedFare(fareEstimate != null ? fareEstimate.getEstimatedFare() : null);

        Ride saved = rideRepository.save(ride);
        return new RideResponse(saved);
    }

    public RideResponse getRideById(Long id) {
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found with ID: " + id));
        return new RideResponse(ride);
    }

    public List<RideResponse> getRidesByPassengerId(Long passengerId) {
        return rideRepository.findByPassengerId(passengerId).stream()
                .map(RideResponse::new)
                .toList();
    }

    public List<RideResponse> getRidesByDriverId(Long driverId) {
        return rideRepository.findByDriverId(driverId).stream()
                .map(RideResponse::new)
                .toList();
    }

    @Transactional
    public RideResponse assignDriver(Long rideId, AssignDriverRequest request) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found with ID: " + rideId));

        if (ride.getStatus() != RideStatus.REQUESTED) {
            throw new InvalidRideStatusException(
                    "Cannot assign driver to ride in state '" + ride.getStatus() + "'. Ride must be in REQUESTED state."
            );
        }

        Long chosenDriverId;
        if (request != null && request.getDriverId() != null) {
            chosenDriverId = request.getDriverId();
        } else {
            // Interservice interaction: Query Driver Service for available drivers
            List<DriverDTO> availableDrivers = driverServiceClient.getAvailableDrivers(null);
            if (availableDrivers == null || availableDrivers.isEmpty()) {
                throw new NoAvailableDriverException("No active and available drivers found in the selected service area.");
            }
            chosenDriverId = availableDrivers.get(0).getDriverId();
        }

        ride.setDriverId(chosenDriverId);
        ride.setStatus(RideStatus.ASSIGNED);

        Ride saved = rideRepository.save(ride);
        return new RideResponse(saved);
    }

    @Transactional
    public RideResponse acceptRide(Long rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found with ID: " + rideId));

        if (ride.getStatus() != RideStatus.ASSIGNED) {
            throw new InvalidRideStatusException(
                    "Cannot accept ride in state '" + ride.getStatus() + "'. Ride must be in ASSIGNED state."
            );
        }

        ride.setStatus(RideStatus.ACCEPTED);
        Ride saved = rideRepository.save(ride);
        return new RideResponse(saved);
    }

    @Transactional
    public RideResponse startRide(Long rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found with ID: " + rideId));

        if (ride.getStatus() != RideStatus.ACCEPTED) {
            throw new InvalidRideStatusException(
                    "Ride must be ACCEPTED before it can start. Current state: '" + ride.getStatus() + "'."
            );
        }

        ride.setStatus(RideStatus.IN_PROGRESS);
        Ride saved = rideRepository.save(ride);
        return new RideResponse(saved);
    }

    @Transactional
    public RideResponse completeRide(Long rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found with ID: " + rideId));

        if (ride.getStatus() != RideStatus.IN_PROGRESS) {
            throw new InvalidRideStatusException(
                    "Ride must be IN_PROGRESS before it can be marked as COMPLETED. Current state: '" + ride.getStatus() + "'."
            );
        }

        // Interservice interaction: Calculate final fare
        FinalFareResponse finalFareResponse = fareServiceClient.getFinalFare(ride.getId(), ride.getDistanceKm());

        ride.setStatus(RideStatus.COMPLETED);
        ride.setFinalFare(finalFareResponse != null ? finalFareResponse.getFinalFare() : ride.getEstimatedFare());
        ride.setCompletedAt(LocalDateTime.now());

        Ride saved = rideRepository.save(ride);
        return new RideResponse(saved);
    }

    @Transactional
    public RideResponse cancelRide(Long rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found with ID: " + rideId));

        if (ride.getStatus() == RideStatus.COMPLETED) {
            throw new InvalidRideStatusException("Completed rides cannot be cancelled.");
        }
        if (ride.getStatus() == RideStatus.CANCELLED) {
            throw new InvalidRideStatusException("Ride is already cancelled.");
        }
        if (ride.getStatus() == RideStatus.IN_PROGRESS) {
            throw new InvalidRideStatusException("Rides currently IN_PROGRESS cannot be cancelled directly.");
        }

        ride.setStatus(RideStatus.CANCELLED);
        Ride saved = rideRepository.save(ride);
        return new RideResponse(saved);
    }
}
