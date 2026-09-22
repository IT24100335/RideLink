package com.ridelink.ride.client;

import com.ridelink.ride.dto.DriverDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;

@Component
public class DriverServiceClient {

    private final RestClient restClient;

    @Value("${services.driver-service.url:http://localhost:8082}")
    private String driverServiceUrl;

    public DriverServiceClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<DriverDTO> getAvailableDrivers(String serviceArea) {
        try {
            String uri = driverServiceUrl + "/api/drivers/available";
            if (serviceArea != null && !serviceArea.isBlank()) {
                uri += "?serviceArea=" + serviceArea;
            }

            return restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<DriverDTO>>() {});
        } catch (Exception ex) {
            // Graceful fallback or empty list if driver service is not reachable
            return Collections.emptyList();
        }
    }
}
