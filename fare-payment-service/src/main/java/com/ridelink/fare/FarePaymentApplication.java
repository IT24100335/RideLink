package com.ridelink.fare;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "RideLink Fare & Payment Service API",
        version = "1.0.0",
        description = "Handles fare calculation algorithms, simulated payments, transaction ledger, and receipt generation.",
        contact = @Contact(name = "RideLink Development Team", email = "support@ridelink.com")
    )
)
public class FarePaymentApplication {
    public static void main(String[] args) {
        SpringApplication.run(FarePaymentApplication.class, args);
    }
}
