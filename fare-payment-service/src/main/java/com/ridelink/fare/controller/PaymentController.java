package com.ridelink.fare.controller;

import com.ridelink.fare.dto.PaymentRequest;
import com.ridelink.fare.dto.PaymentResponse;
import com.ridelink.fare.dto.ReceiptResponse;
import com.ridelink.fare.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payment & Receipt Ledger", description = "Endpoints for simulated payment processing, ledger lookup, and receipts")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @Operation(summary = "Process and record a simulated payment for a completed ride")
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.processPayment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get payment transaction record by payment ID")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long id) {
        PaymentResponse response = paymentService.getPaymentById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/receipt")
    @Operation(summary = "Retrieve official itemized receipt by payment ID")
    public ResponseEntity<ReceiptResponse> getReceiptByPaymentId(@PathVariable Long id) {
        ReceiptResponse response = paymentService.getReceiptByPaymentId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ride/{rideId}/receipt")
    @Operation(summary = "Retrieve official itemized receipt by ride ID")
    public ResponseEntity<ReceiptResponse> getReceiptByRideId(@PathVariable Long rideId) {
        ReceiptResponse response = paymentService.getReceiptByRideId(rideId);
        return ResponseEntity.ok(response);
    }
}
