package com.ridelink.fare.service;

import com.ridelink.fare.dto.PaymentRequest;
import com.ridelink.fare.dto.PaymentResponse;
import com.ridelink.fare.dto.ReceiptResponse;
import com.ridelink.fare.entity.Payment;
import com.ridelink.fare.entity.PaymentStatus;
import com.ridelink.fare.entity.Receipt;
import com.ridelink.fare.exception.PaymentFailedException;
import com.ridelink.fare.exception.ResourceNotFoundException;
import com.ridelink.fare.repository.PaymentRepository;
import com.ridelink.fare.repository.ReceiptRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReceiptRepository receiptRepository;

    public PaymentService(PaymentRepository paymentRepository, ReceiptRepository receiptRepository) {
        this.paymentRepository = paymentRepository;
        this.receiptRepository = receiptRepository;
    }

    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
        String transactionRef = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        if (request.isSimulateFailure()) {
            Payment failedPayment = new Payment();
            failedPayment.setRideId(request.getRideId());
            failedPayment.setPassengerId(request.getPassengerId());
            failedPayment.setAmount(request.getAmount());
            failedPayment.setPaymentMethod(request.getPaymentMethod());
            failedPayment.setStatus(PaymentStatus.FAILED);
            failedPayment.setTransactionRef(transactionRef);
            failedPayment.setPaidAt(LocalDateTime.now());
            paymentRepository.save(failedPayment);

            throw new PaymentFailedException("Simulated payment transaction failed: Insufficient funds or gateway timeout.");
        }

        Payment payment = new Payment();
        payment.setRideId(request.getRideId());
        payment.setPassengerId(request.getPassengerId());
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setTransactionRef(transactionRef);
        payment.setPaidAt(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        // Generate Itemized Receipt
        String datePrefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String receiptNumber = "RCP-" + datePrefix + "-" + String.format("%04d", savedPayment.getId());

        Receipt receipt = new Receipt();
        receipt.setReceiptNumber(receiptNumber);
        receipt.setPayment(savedPayment);
        receipt.setRideId(savedPayment.getRideId());
        receipt.setPassengerId(savedPayment.getPassengerId());
        receipt.setTotalAmount(savedPayment.getAmount());
        receipt.setCalculationBreakdown("Payment via " + savedPayment.getPaymentMethod() + " | Ref: " + transactionRef);
        receipt.setIssuedAt(LocalDateTime.now());

        receiptRepository.save(receipt);
        savedPayment.setReceipt(receipt);

        return new PaymentResponse(savedPayment);
    }

    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment record not found with ID: " + id));
        return new PaymentResponse(payment);
    }

    public ReceiptResponse getReceiptByPaymentId(Long paymentId) {
        Receipt receipt = receiptRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Receipt not found for Payment ID: " + paymentId));
        return new ReceiptResponse(receipt);
    }

    public ReceiptResponse getReceiptByRideId(Long rideId) {
        Receipt receipt = receiptRepository.findByRideId(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Receipt not found for Ride ID: " + rideId));
        return new ReceiptResponse(receipt);
    }
}
