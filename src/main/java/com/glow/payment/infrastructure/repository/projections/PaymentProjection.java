package com.glow.payment.infrastructure.repository.projections;

import java.time.Instant;
import java.util.UUID;

/**
 * Read-only projection of Payment for query responses.
 * Used to fetch payment data without loading all relationships.
 */
public class PaymentProjection {
    
    public UUID id;
    public Instant createdAt;
    public Instant updatedAt;
    public String stripePaymentIntentId;
    public Integer amount;
    public UUID customerId;
    public UUID orderId;
    public String status;
    
    public PaymentProjection() {
    }
    
    public PaymentProjection(UUID id, Instant createdAt, Instant updatedAt, String stripePaymentIntentId,
                             Integer amount, UUID customerId, UUID orderId, String status) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.stripePaymentIntentId = stripePaymentIntentId;
        this.amount = amount;
        this.customerId = customerId;
        this.orderId = orderId;
        this.status = status;
    }
}
