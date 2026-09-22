package com.ridelink.fare.service;

import com.ridelink.fare.dto.PaymentRequest;
import com.ridelink.fare.dto.PaymentResponse;
import com.ridelink.fare.dto.ReceiptResponse;
import com.ridelink.fare.entity.Payment;
import com.ridelink.fare.entity.PaymentMethod;
import com.ridelink.fare.entity.PaymentStatus;
import com.ridelink.fare.entity.Receipt;
import com.ridelink.fare.exception.PaymentFailedException;
import com.ridelink.fare.exception.ResourceNotFoundException;
import com.ridelink.fare.repository.PaymentRepository;
import com.ridelink.fare.repository.ReceiptRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ReceiptRepository receiptRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Payment samplePayment;
    private Receipt sampleReceipt;
    private PaymentRequest paymentRequest;

    @BeforeEach
    void setUp() {
        samplePayment = new Payment(
                1L,
                100L,
                10L,
                650.0,
                PaymentStatus.SUCCESS,
                PaymentMethod.CARD,
                "TXN-ABC12345",
                LocalDateTime.now()
        );

        sampleReceipt = new Receipt(
                1L,
                "RCP-20260922-0001",
                samplePayment,
                100L,
                10L,
                650.0,
                "Payment via CARD",
                LocalDateTime.now()
        );

        paymentRequest = new PaymentRequest(100L, 10L, 650.0, PaymentMethod.CARD);
    }

    @Test
    @DisplayName("Should successfully record simulated payment and generate receipt")
    void processPayment_Success() {
        when(paymentRepository.save(any(Payment.class))).thenReturn(samplePayment);
        when(receiptRepository.save(any(Receipt.class))).thenReturn(sampleReceipt);

        PaymentResponse response = paymentService.processPayment(paymentRequest);

        assertNotNull(response);
        assertEquals(PaymentStatus.SUCCESS, response.getStatus());
        assertEquals(650.0, response.getAmount());
        assertEquals("TXN-ABC12345", response.getTransactionRef());

        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(receiptRepository, times(1)).save(any(Receipt.class));
    }

    @Test
    @DisplayName("Should simulate payment failure when simulateFailure flag is set")
    void processPayment_SimulatedFailure_ThrowsException() {
        paymentRequest.setSimulateFailure(true);
        when(paymentRepository.save(any(Payment.class))).thenReturn(samplePayment);

        assertThrows(PaymentFailedException.class, () -> paymentService.processPayment(paymentRequest));
        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(receiptRepository, never()).save(any(Receipt.class));
    }

    @Test
    @DisplayName("Should retrieve receipt by payment ID")
    void getReceiptByPaymentId_Success() {
        when(receiptRepository.findByPaymentId(1L)).thenReturn(Optional.of(sampleReceipt));

        ReceiptResponse response = paymentService.getReceiptByPaymentId(1L);

        assertNotNull(response);
        assertEquals("RCP-20260922-0001", response.getReceiptNumber());
        assertEquals(650.0, response.getTotalAmount());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when receipt does not exist")
    void getReceiptByPaymentId_NotFound_ThrowsException() {
        when(receiptRepository.findByPaymentId(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> paymentService.getReceiptByPaymentId(99L));
    }
}
