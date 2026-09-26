package com.ridelink.ride.client;

import com.ridelink.ride.dto.FareEstimateRequest;
import com.ridelink.ride.dto.FareEstimateResponse;
import com.ridelink.ride.dto.FinalFareRequest;
import com.ridelink.ride.dto.FinalFareResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class FareServiceClient {

    private final RestClient restClient;

    @Value("${services.fare-service.url:http://localhost:8084}")
    private String fareServiceUrl;

    public FareServiceClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public FareEstimateResponse getFareEstimate(String pickup, String destination, Double distanceKm) {
        try {
            FareEstimateRequest request = new FareEstimateRequest(pickup, destination, distanceKm);
            return restClient.post()
                    .uri(fareServiceUrl + "/api/fares/estimate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(FareEstimateResponse.class);
        } catch (Exception ex) {
            // Documented formula fallback: Rs. 150 + (Distance * 100)
            double fallback = 150.0 + (distanceKm != null ? distanceKm * 100.0 : 0.0);
            return new FareEstimateResponse(150.0, 100.0, distanceKm, fallback);
        }
    }

    public FinalFareResponse getFinalFare(Long rideId, Double finalDistanceKm) {
        try {
            FinalFareRequest request = new FinalFareRequest(rideId, finalDistanceKm);
            return restClient.post()
                    .uri(fareServiceUrl + "/api/fares/final")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(FinalFareResponse.class);
        } catch (Exception ex) {
            double fallback = 150.0 + (finalDistanceKm != null ? finalDistanceKm * 100.0 : 0.0);
            return new FinalFareResponse(rideId, fallback, "Base 150 + (Distance " + finalDistanceKm + " * 100)");
        }
    }
}
