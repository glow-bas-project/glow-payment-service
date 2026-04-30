package com.glow.payment.domain.repository;

import com.glow.payment.domain.model.Payment;
import com.glow.payment.domain.model.PaymentStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Domain repository interface for Payment aggregate.
 * Defines the contract for persistence operations on Payments.
 */
public interface PaymentRepository {
    
    void save(Payment payment);
    
    void update(Payment payment);
    
    Optional<Payment> findById(UUID id);
    
    Optional<Payment> findByStripePaymentIntentId(String stripePaymentIntentId);
    
    List<Payment> findByOrderId(UUID orderId);
    
    List<Payment> findByCustomerId(UUID customerId);
    
    List<Payment> findByStatus(PaymentStatus status);
    
    boolean deleteById(UUID id);
}

