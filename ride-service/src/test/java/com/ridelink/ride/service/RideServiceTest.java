package com.ridelink.ride.service;

import com.ridelink.ride.client.DriverServiceClient;
import com.ridelink.ride.client.FareServiceClient;
import com.ridelink.ride.dto.*;
import com.ridelink.ride.entity.Ride;
import com.ridelink.ride.entity.RideStatus;
import com.ridelink.ride.exception.InvalidRideStatusException;
import com.ridelink.ride.exception.NoAvailableDriverException;
import com.ridelink.ride.repository.RideRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RideServiceTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private DriverServiceClient driverServiceClient;

    @Mock
    private FareServiceClient fareServiceClient;

    @InjectMocks
    private RideService rideService;

    private Ride sampleRide;
    private CreateRideRequest createRequest;

    @BeforeEach
    void setUp() {
        sampleRide = new Ride(
                1L,
                10L,
                null,
                "Colombo Fort",
                "Bambalapitiya",
                5.0,
                RideStatus.REQUESTED,
                650.0,
                null);

        createRequest = new CreateRideRequest(10L, "Colombo Fort", "Bambalapitiya", 5.0);
    }

    @Test
    @DisplayName("Should create ride and calculate estimated fare via Fare Service")
    void createRide_Success() {
        when(fareServiceClient.getFareEstimate("Colombo Fort", "Bambalapitiya", 5.0))
                .thenReturn(new FareEstimateResponse(150.0, 100.0, 5.0, 650.0));
        when(rideRepository.save((Ride) notNull(Ride.class))).thenReturn(new Ride(
                1L,
                10L,
                null,
                "Colombo Fort",
                "Bambalapitiya",
                5.0,
                RideStatus.REQUESTED,
                650.0,
                null));

        RideResponse response = rideService.createRide(createRequest);

        assertNotNull(response);
        assertEquals(RideStatus.REQUESTED, response.getStatus());
        assertEquals(650.0, response.getEstimatedFare());
        verify(fareServiceClient, times(1)).getFareEstimate(anyString(), anyString(), anyDouble());
        verify(rideRepository, times(1)).save(any(Ride.class));
    }

    @Test
    @DisplayName("Should assign driver from available driver list")
    void assignDriver_Success() {
        DriverDTO driverDTO = new DriverDTO(2L, 5L, "B1234567", "CAR", "CAB-1234", "Colombo", "AVAILABLE");
        when(rideRepository.findById(1L)).thenReturn(Optional.of(sampleRide));
        when(driverServiceClient.getAvailableDrivers(null)).thenReturn(List.of(driverDTO));
        when(rideRepository.save(notNull(Ride.class))).thenReturn(sampleRide);

        RideResponse response = rideService.assignDriver(1L, null);

        assertNotNull(response);
        assertEquals(RideStatus.ASSIGNED, sampleRide.getStatus());
        assertEquals(2L, sampleRide.getDriverId());
    }

    @Test
    @DisplayName("Negative Scenario 1: Should throw NoAvailableDriverException when driver pool is empty")
    void assignDriver_NoDriversAvailable_ThrowsException() {
        when(rideRepository.findById(1L)).thenReturn(Optional.of(sampleRide));
        when(driverServiceClient.getAvailableDrivers(null)).thenReturn(Collections.emptyList());

        assertThrows(NoAvailableDriverException.class, () -> rideService.assignDriver(1L, null));
    }

    @Test
    @DisplayName("Negative Scenario 2: Should reject invalid status jump from REQUESTED directly to COMPLETED")
    void completeRide_InvalidTransitionFromRequested_ThrowsException() {
        when(rideRepository.findById(1L)).thenReturn(Optional.of(sampleRide));

        InvalidRideStatusException exception = assertThrows(
                InvalidRideStatusException.class,
                () -> rideService.completeRide(1L));

        assertTrue(exception.getMessage().contains("must be IN_PROGRESS"));
    }

    @Test
    @DisplayName("Should complete ride when in IN_PROGRESS status and call Fare Service for final fare")
    void completeRide_Success() {
        sampleRide.setStatus(RideStatus.IN_PROGRESS);
        sampleRide.setDriverId(2L);

        when(rideRepository.findById(1L)).thenReturn(Optional.of(sampleRide));
        when(fareServiceClient.getFinalFare(1L, 5.0))
                .thenReturn(new FinalFareResponse(1L, 650.0, "Base 150 + 5*100"));
        when(rideRepository.save(notNull(Ride.class))).thenReturn(sampleRide);

        RideResponse response = rideService.completeRide(1L);

        assertNotNull(response);
        assertEquals(RideStatus.COMPLETED, sampleRide.getStatus());
        assertEquals(650.0, sampleRide.getFinalFare());
        assertNotNull(sampleRide.getCompletedAt());
    }
}
