package com.ridelink.fare.service;

import com.ridelink.fare.dto.FareEstimateRequest;
import com.ridelink.fare.dto.FareEstimateResponse;
import com.ridelink.fare.dto.FinalFareRequest;
import com.ridelink.fare.dto.FinalFareResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class FareServiceTest {

    private FareService fareService;

    @BeforeEach
    void setUp() {
        fareService = new FareService();
        ReflectionTestUtils.setField(fareService, "baseFare", 150.0);
        ReflectionTestUtils.setField(fareService, "ratePerKm", 100.0);
    }

    @Test
    @DisplayName("Should accurately calculate fare estimate: Base 150 + 5km * 100 = 650")
    void calculateEstimate_Success() {
        FareEstimateRequest request = new FareEstimateRequest("Colombo Fort", "Bambalapitiya", 5.0);

        FareEstimateResponse response = fareService.calculateEstimate(request);

        assertNotNull(response);
        assertEquals(150.0, response.getBaseFare());
        assertEquals(100.0, response.getRatePerKm());
        assertEquals(5.0, response.getDistanceKm());
        assertEquals(650.0, response.getEstimatedFare());
    }

    @Test
    @DisplayName("Should accurately calculate final fare: Base 150 + 8km * 100 = 950")
    void calculateFinalFare_Success() {
        FinalFareRequest request = new FinalFareRequest(1L, 8.0);

        FinalFareResponse response = fareService.calculateFinalFare(request);

        assertNotNull(response);
        assertEquals(1L, response.getRideId());
        assertEquals(950.0, response.getFinalFare());
        assertTrue(response.getCalculationBreakdown().contains("Total: Rs. 950.00"));
    }

    @Test
    @DisplayName("Negative Scenario 4: Should throw IllegalArgumentException on zero or negative distance")
    void calculateEstimate_InvalidNegativeDistance_ThrowsException() {
        FareEstimateRequest request = new FareEstimateRequest("Colombo Fort", "Bambalapitiya", -5.0);

        assertThrows(IllegalArgumentException.class, () -> fareService.calculateEstimate(request));
    }
}
