package com.ridelink.account;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "RideLink Account Service API",
        version = "1.0.0",
        description = "Handles passenger/driver account management, authentication, and role authorization for RideLink platform.",
        contact = @Contact(name = "RideLink Development Team", email = "support@ridelink.com")
    )
)
public class AccountApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountApplication.class, args);
    }
}
