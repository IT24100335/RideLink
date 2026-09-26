package com.ridelink.ride;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "RideLink Ride Management Service API",
        version = "1.0.0",
        description = "Orchestrates ride requests, driver assignment, and the end-to-end ride lifecycle state machine.",
        contact = @Contact(name = "RideLink Development Team", email = "support@ridelink.com")
    )
)
public class RideApplication {
    public static void main(String[] args) {
        SpringApplication.run(RideApplication.class, args);
    }
}
